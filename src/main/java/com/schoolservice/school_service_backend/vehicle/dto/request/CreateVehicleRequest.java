package com.schoolservice.school_service_backend.vehicle.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateVehicleRequest {

    @NotBlank
    private String plateNumber;

    @NotBlank
    private String brand;

    @NotBlank
    private String model;

    @Min(1)
    private int capacity;
}
