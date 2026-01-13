package com.schoolservice.school_service_backend.user.dto;

import java.util.List;
import java.util.UUID;

public record PendingUserResponse(
        UUID id,
        String email,
        String firstName,
        String lastName,
        List<String> role
) {}
