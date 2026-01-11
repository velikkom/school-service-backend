package com.schoolservice.school_service_backend.security.service;

import com.schoolservice.school_service_backend.security.dto.LoginRequest;
import com.schoolservice.school_service_backend.security.dto.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);
}
