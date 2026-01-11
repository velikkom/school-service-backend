package com.schoolservice.school_service_backend.security.service;

import com.schoolservice.school_service_backend.security.dto.LoginRequest;
import com.schoolservice.school_service_backend.security.dto.LoginResponse;
import com.schoolservice.school_service_backend.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Override
    public LoginResponse login(LoginRequest request) {

        // 1️⃣ Authentication
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.email(),
                                request.password()
                        )
                );

        // 2️⃣ JWT
        String token = jwtUtils.generateJwtToken(
                (org.springframework.security.core.userdetails.User)
                        authentication.getPrincipal()
        );

        // 3️⃣ Roles
        List<String> roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return new LoginResponse(
                token,
                request.email(),
                roles
        );
    }
}
