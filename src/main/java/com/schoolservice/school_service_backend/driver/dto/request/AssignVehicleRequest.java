package com.schoolservice.school_service_backend.driver.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AssignVehicleRequest {

    @NotNull
    private UUID vehicleId;
}