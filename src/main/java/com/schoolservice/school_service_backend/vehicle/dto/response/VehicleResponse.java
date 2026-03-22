package com.schoolservice.school_service_backend.vehicle.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class VehicleResponse {

    private UUID id;
    private String plateNumber;
    private String brand;
    private String model;
    private int capacity;
    private boolean active;
}