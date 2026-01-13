package com.schoolservice.school_service_backend.user.dto;

import com.schoolservice.school_service_backend.user.enums.ApprovalStatus;
import com.schoolservice.school_service_backend.user.enums.RoleType;

import java.util.Set;
import java.util.UUID;

public record AdminUserResponse(
        UUID id,
        String email,
        String firstName,
        String lastName,
        Set<RoleType> roles,
        boolean active,
        ApprovalStatus approvalStatus
) {
}
