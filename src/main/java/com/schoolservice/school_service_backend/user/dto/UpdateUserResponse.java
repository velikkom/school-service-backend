package com.schoolservice.school_service_backend.user.dto;

import com.schoolservice.school_service_backend.user.enums.RoleType;

public record UpdateUserResponse(String firstName,
                                 String lastName,
                                 String email,
                                 String phone,
                                 RoleType role,
                                 Boolean active) {
}
