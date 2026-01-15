package com.schoolservice.school_service_backend.user.dto;

import jakarta.validation.constraints.Email;

public record UpdateProfileRequest(

        String firstName,
        String lastName,

        @Email
        String email
) {}
