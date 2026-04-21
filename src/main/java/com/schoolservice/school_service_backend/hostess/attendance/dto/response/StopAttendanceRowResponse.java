package com.schoolservice.school_service_backend.hostess.attendance.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record StopAttendanceRowResponse(
        UUID routeStopId,
        int stopOrder,
        String addressText,
        UUID studentId,
        String studentFirstName,
        String studentLastName,
        LocalDateTime arrivedAt,
        LocalDateTime departedAt
) {
}
