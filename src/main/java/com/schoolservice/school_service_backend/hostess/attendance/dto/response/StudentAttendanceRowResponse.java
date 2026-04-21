package com.schoolservice.school_service_backend.hostess.attendance.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record StudentAttendanceRowResponse(
        UUID studentId,
        String firstName,
        String lastName,
        UUID routeStopId,
        Integer stopOrder,
        boolean pickedUp,
        LocalDateTime pickedUpAt,
        boolean droppedOff,
        LocalDateTime droppedOffAt
) {
}
