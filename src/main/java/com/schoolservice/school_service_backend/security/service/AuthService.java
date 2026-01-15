package com.schoolservice.school_service_backend.security.service;

import com.schoolservice.school_service_backend.auth.dto.ForgotPasswordRequest;
import com.schoolservice.school_service_backend.auth.dto.ResetPasswordRequest;
import com.schoolservice.school_service_backend.security.dto.LoginRequest;
import com.schoolservice.school_service_backend.security.dto.LoginResponse;
import com.schoolservice.school_service_backend.user.dto.ChangePasswordRequest;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    void forgotPassword(ForgotPasswordRequest request);

    void resetPassword(ResetPasswordRequest request);

    void changePassword(String email, ChangePasswordRequest request);

}
