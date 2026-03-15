package com.schoolservice.school_service_backend.hostess.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateHostessAssignmentRequest {

    @NotNull(message = "Route id is required")
    private UUID routeId;

    @NotNull(message = "Vehicle id is required")
    private UUID vehicleId;

    @NotNull(message = "Day of week is required")
    private DayOfWeek dayOfWeek;

    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    private LocalTime endTime;

    private String notes;
}