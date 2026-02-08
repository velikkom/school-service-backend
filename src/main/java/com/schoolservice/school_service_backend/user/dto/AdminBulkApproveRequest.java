package com.schoolservice.school_service_backend.user.dto;

import com.schoolservice.school_service_backend.user.enums.RoleType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record AdminBulkApproveRequest(

        @NotEmpty(message = "User id list cannot be empty")
        List<UUID> userIds,

        @NotNull(message = "Role is required")
        RoleType role
) {}
