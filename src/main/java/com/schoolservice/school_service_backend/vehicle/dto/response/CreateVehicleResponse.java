package com.schoolservice.school_service_backend.vehicle.dto.response;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateVehicleResponse {

    @NotBlank
    private String plateNumber;

    private String brand;
    private String model;

    @Min(1)
    private int capacity;
}