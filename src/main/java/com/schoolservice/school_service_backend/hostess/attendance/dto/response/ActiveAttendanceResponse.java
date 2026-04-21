package com.schoolservice.school_service_backend.hostess.attendance.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record ActiveAttendanceResponse(
        TripSummaryResponse trip,
        StopAttendanceRowResponse currentStop,
        List<StopAttendanceRowResponse> stops,
        List<StudentAttendanceRowResponse> students
) {
}
