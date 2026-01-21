package com.schoolservice.school_service_backend.security.service;

import com.schoolservice.school_service_backend.auth.dto.ForgotPasswordRequest;
import com.schoolservice.school_service_backend.auth.dto.ResetPasswordRequest;
import com.schoolservice.school_service_backend.auth.entity.PasswordResetToken;
import com.schoolservice.school_service_backend.auth.repository.PasswordResetTokenRepository;
import com.schoolservice.school_service_backend.common.exception.BusinessException;
import com.schoolservice.school_service_backend.common.exception.ResourceNotFoundException;
import com.schoolservice.school_service_backend.notification.service.EmailService;
import com.schoolservice.school_service_backend.security.dto.LoginRequest;
import com.schoolservice.school_service_backend.security.dto.LoginResponse;
import com.schoolservice.school_service_backend.security.jwt.JwtUtils;
import com.schoolservice.school_service_backend.user.dto.ChangePasswordRequest;
import com.schoolservice.school_service_backend.user.entity.User;
import com.schoolservice.school_service_backend.user.enums.ApprovalStatus;
import com.schoolservice.school_service_backend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    // 🔐 LOGIN
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    // 🔁 PASSWORD RESET
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    // =========================
// LOGIN
// =========================
    @Override
    public LoginResponse login(LoginRequest request) {

        // 1) USER VAR MI? (email üzerinden)
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BusinessException("Kullanıcı bulunamadı"));

        // 2) ADMIN ONAYI + AKTİFLİK KONTROLÜ (TOKEN ÜRETMEDEN ÖNCE)
        if (user.getApprovalStatus() == ApprovalStatus.PENDING) {
            throw new BusinessException("Admin onayı bekleniyor");
        }

        if (!Boolean.TRUE.equals(user.isActive())) {
            throw new BusinessException("Hesap pasif durumda");
        }

        // 3) AUTHENTICATE (email+password)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        // 4) JWT ÜRET
        String token = jwtUtils.generateJwtToken(
                (org.springframework.security.core.userdetails.User)
                        authentication.getPrincipal()
        );

        // 5) ROLLERİ AL
        List<String> roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        // 6) RESPONSE
        return new LoginResponse(
                token,
                user.getEmail(),
                roles
        );
    }


    // =========================
    // FORGOT PASSWORD
    // =========================

    @Override
    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() ->
                        new BusinessException("Bu e-posta ile kayıtlı kullanıcı bulunamadı")
                );

        // 🔥 Eski tokenları sil
        tokenRepository.deleteByUserId(user.getId());

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .used(false)
                .build();

        tokenRepository.save(resetToken);

        // 📧 MAIL GÖNDER (kritik satır)
        emailService.sendPasswordResetEmail(user.getEmail(), token);
    }





    // =========================
    // RESET PASSWORD
    // =========================
    @Override
    public void resetPassword(ResetPasswordRequest request) {

        PasswordResetToken resetToken = tokenRepository.findByToken(request.token())
                .orElseThrow(() ->
                        new BusinessException("Invalid reset token")
                );

        if (resetToken.isUsed()) {
            throw new BusinessException("Token already used");
        }

        if (resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Token expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.newPassword()));

        resetToken.setUsed(true);

        userRepository.save(user);
        tokenRepository.save(resetToken);
    }

    @Override
    public void changePassword(String email, ChangePasswordRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );

        if (!passwordEncoder.matches(
                request.currentPassword(),
                user.getPassword()
        )) {
            throw new BusinessException("Current password is incorrect");
        }

        user.setPassword(
                passwordEncoder.encode(request.newPassword())
        );

        userRepository.save(user);
    }

}
