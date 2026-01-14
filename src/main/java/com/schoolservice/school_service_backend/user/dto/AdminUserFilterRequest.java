package com.schoolservice.school_service_backend.user.dto;

import com.schoolservice.school_service_backend.user.enums.ApprovalStatus;
import com.schoolservice.school_service_backend.user.enums.RoleType;

import java.time.LocalDate;

public record AdminUserFilterRequest(
        ApprovalStatus approvalStatus,
        Boolean active,
        RoleType role,
        String email,
        String firstName,
        String lastName,
        LocalDate createdAtFrom,
        LocalDate createdAtTo,
        Boolean includeDeleted
) {
}
