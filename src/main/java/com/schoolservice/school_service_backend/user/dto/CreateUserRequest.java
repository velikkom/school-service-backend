package com.schoolservice.school_service_backend.user.dto;

import com.schoolservice.school_service_backend.user.enums.RoleType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Size(min = 6)
        String password,

        @NotBlank
        String firstName,

        @NotBlank
        String lastName

//        @NotNull(message = "Requested user type is required")
//        RoleType requestedUserType
) {}
