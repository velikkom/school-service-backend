package com.schoolservice.school_service_backend.hostess.attendance.service;

import com.schoolservice.school_service_backend.hostess.attendance.dto.response.ActiveAttendanceResponse;

import java.util.UUID;

public interface HostessAttendanceService {

    ActiveAttendanceResponse getActiveScreen(String authenticatedUserEmail);

    ActiveAttendanceResponse markStopArrived(String authenticatedUserEmail, UUID routeStopId);

    ActiveAttendanceResponse markStopDeparted(String authenticatedUserEmail, UUID routeStopId);

    ActiveAttendanceResponse markStudentPickup(String authenticatedUserEmail, UUID studentId);

    ActiveAttendanceResponse markStudentDropoff(String authenticatedUserEmail, UUID studentId);
}
