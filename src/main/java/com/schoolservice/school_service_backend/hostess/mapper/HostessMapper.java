package com.schoolservice.school_service_backend.hostess.mapper;

import com.schoolservice.school_service_backend.hostess.dto.response.HostessAssignmentResponse;
import com.schoolservice.school_service_backend.hostess.dto.response.HostessResponse;
import com.schoolservice.school_service_backend.hostess.entity.Hostess;
import com.schoolservice.school_service_backend.hostess.entity.HostessAssignment;

public class HostessMapper {

    private HostessMapper() {
    }

    public static HostessResponse toHostessResponse(Hostess hostess) {

        return HostessResponse.builder()
                .id(hostess.getId())
                .userId(hostess.getUser().getId())
                .firstName(hostess.getUser().getFirstName())
                .lastName(hostess.getUser().getLastName())
                .email(hostess.getUser().getEmail())
                .phoneNumber(hostess.getPhone())
                .emergencyContactName(hostess.getEmergencyContact())
                .emergencyContactPhone(hostess.getPhone())
                .active(hostess.isActive())
                .createdAt(hostess.getCreatedAt())
                .build();
    }

    public static HostessAssignmentResponse toHostessAssignmentResponse(HostessAssignment assignment) {

        return HostessAssignmentResponse.builder()
                .id(assignment.getId())
                .hostessId(assignment.getHostess().getId())
                .routeId(assignment.getRoute().getId())
                .routeName(assignment.getRoute().getName())
                .routeCode(assignment.getRoute().getCode())
                .vehicleId(assignment.getVehicle().getId())
                .plateNumber(assignment.getVehicle().getPlateNumber())
                .dayOfWeek(assignment.getDayOfWeek())
                .startTime(assignment.getStartTime())
                .endTime(assignment.getEndTime())
                .notes(assignment.getNotes())
                .active(assignment.isActive())
                .createdAt(assignment.getCreatedAt())
                .build();
    }
}