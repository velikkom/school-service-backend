package com.schoolservice.school_service_backend.auth.controller;

import com.schoolservice.school_service_backend.auth.dto.ForgotPasswordRequest;
import com.schoolservice.school_service_backend.auth.dto.ResetPasswordRequest;
import com.schoolservice.school_service_backend.common.response.ResponseWrapper;
import com.schoolservice.school_service_backend.security.dto.LoginRequest;
import com.schoolservice.school_service_backend.security.dto.LoginResponse;
import com.schoolservice.school_service_backend.security.service.AuthService;
import com.schoolservice.school_service_backend.user.dto.ChangePasswordRequest;
import com.schoolservice.school_service_backend.user.dto.CreateUserRequest;
import com.schoolservice.school_service_backend.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Request;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication", description = "Login & JWT operations")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @Operation(
            summary = "User Login",
            description = "Authenticate user and return JWT token"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "400", description = "Invalid credentials"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(
                authService.login(request)
        );
    }

    @PostMapping("/register")
    @PermitAll
    public ResponseEntity<ResponseWrapper<Void>> register(
            @Valid @RequestBody CreateUserRequest request
    ) {
        userService.registerUser(request);
        return ResponseEntity.ok(
                ResponseWrapper.success(
                        null,
                        "Registration successful. Waiting for admin approval."
                )
        );
    }

    /**
     * Forgot Password
     */
    @PostMapping("/forgot-password")
    @PermitAll
    public ResponseEntity<ResponseWrapper<Void>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request
    ) {
        authService.forgotPassword(request);
        return ResponseEntity.ok(
                ResponseWrapper.success(
                        null,
                        "Password reset email sent successfully"
                )
        );
    }

    /**
     * Reset Password
     */
    @PostMapping("/reset-password")
    @PermitAll
    public ResponseEntity<ResponseWrapper<Void>> resetPassWord(
            @Valid @RequestBody ResetPasswordRequest request
    ) {
        authService.resetPassword(request);
        return ResponseEntity.ok(
                ResponseWrapper.success(
                        null,
                        "Password reset successfully"
                )
        );
    }

    @PutMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseWrapper<Void>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            Authentication authentication
    ) {
        authService.changePassword(authentication.getName(), request);
        return ResponseEntity.ok(
                ResponseWrapper.success(null, "Password changed successfully")
        );
    }

}
