package com.schoolservice.school_service_backend.vehicle.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateVehicleRequest {

    private String brand;
    private String model;

    @Min(1)
    private int capacity;
}