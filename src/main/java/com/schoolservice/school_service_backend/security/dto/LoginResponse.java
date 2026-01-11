package com.schoolservice.school_service_backend.security.dto;

import java.util.List;

public record LoginResponse(

        String token,
        String email,
        List<String> roles
) {}
