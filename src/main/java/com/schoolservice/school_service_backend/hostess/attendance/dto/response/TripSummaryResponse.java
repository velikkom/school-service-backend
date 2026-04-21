package com.schoolservice.school_service_backend.hostess.attendance.dto.response;

import com.schoolservice.school_service_backend.trip.enums.TripStatus;
import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public record TripSummaryResponse(
        UUID tripId,
        TripStatus status,
        LocalDate serviceDate,
        UUID routeId,
        String routeName,
        String routeCode,
        UUID vehicleId,
        String vehiclePlate,
        UUID currentRouteStopId
) {
}
