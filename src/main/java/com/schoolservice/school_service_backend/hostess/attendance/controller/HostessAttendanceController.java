package com.schoolservice.school_service_backend.hostess.attendance.controller;

import com.schoolservice.school_service_backend.common.response.ResponseWrapper;
import com.schoolservice.school_service_backend.hostess.attendance.dto.response.ActiveAttendanceResponse;
import com.schoolservice.school_service_backend.hostess.attendance.service.HostessAttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/hostess/attendance")
@RequiredArgsConstructor
@Tag(name = "Hostess attendance", description = "Active trip, stops, and student pickup/drop-off")
public class HostessAttendanceController {

    private final HostessAttendanceService hostessAttendanceService;

    @GetMapping("/active")
    @Operation(summary = "Active attendance screen")
    public ResponseEntity<ResponseWrapper<ActiveAttendanceResponse>> getActive(Authentication authentication) {
        String email = (String) authentication.getPrincipal();
        ActiveAttendanceResponse body = hostessAttendanceService.getActiveScreen(email);
        return ResponseEntity.ok(ResponseWrapper.success(body, "Active attendance loaded"));
    }

    @PostMapping("/stops/{stopId}/arrive")
    @Operation(summary = "Mark stop arrived")
    public ResponseEntity<ResponseWrapper<ActiveAttendanceResponse>> arrive(
            Authentication authentication,
            @PathVariable UUID stopId
    ) {
        String email = (String) authentication.getPrincipal();
        ActiveAttendanceResponse body = hostessAttendanceService.markStopArrived(email, stopId);
        return ResponseEntity.ok(ResponseWrapper.success(body, "Stop marked as arrived"));
    }

    @PostMapping("/stops/{stopId}/depart")
    @Operation(summary = "Mark stop departed")
    public ResponseEntity<ResponseWrapper<ActiveAttendanceResponse>> depart(
            Authentication authentication,
            @PathVariable UUID stopId
    ) {
        String email = (String) authentication.getPrincipal();
        ActiveAttendanceResponse body = hostessAttendanceService.markStopDeparted(email, stopId);
        return ResponseEntity.ok(ResponseWrapper.success(body, "Stop marked as departed"));
    }

    @PostMapping("/students/{studentId}/pickup")
    @Operation(summary = "Mark student picked up")
    public ResponseEntity<ResponseWrapper<ActiveAttendanceResponse>> pickup(
            Authentication authentication,
            @PathVariable UUID studentId
    ) {
        String email = (String) authentication.getPrincipal();
        ActiveAttendanceResponse body = hostessAttendanceService.markStudentPickup(email, studentId);
        return ResponseEntity.ok(ResponseWrapper.success(body, "Student picked up"));
    }

    @PostMapping("/students/{studentId}/dropoff")
    @Operation(summary = "Mark student dropped off")
    public ResponseEntity<ResponseWrapper<ActiveAttendanceResponse>> dropoff(
            Authentication authentication,
            @PathVariable UUID studentId
    ) {
        String email = (String) authentication.getPrincipal();
        ActiveAttendanceResponse body = hostessAttendanceService.markStudentDropoff(email, studentId);
        return ResponseEntity.ok(ResponseWrapper.success(body, "Student dropped off"));
    }
}
