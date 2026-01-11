package com.schoolservice.school_service_backend.common.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
        int status,
        String message,
        LocalDateTime timestamp
) {}
