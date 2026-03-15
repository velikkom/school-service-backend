package com.schoolservice.school_service_backend.hostess.dto.response;

import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HostessAssignmentResponse {

    private UUID id;

    private UUID hostessId;

    private UUID routeId;

    private String routeName;

    private String routeCode;

    private UUID vehicleId;

    private String plateNumber;

    private DayOfWeek dayOfWeek;

    private LocalTime startTime;

    private LocalTime endTime;

    private String notes;

    private boolean active;

    private LocalDateTime createdAt;
}