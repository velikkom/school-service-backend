package com.schoolservice.school_service_backend.driver.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class DriverResponse {

    private UUID id;

    private String firstName;
    private String lastName;

    private String phoneNumber;
    private String licenseNumber;

    private UUID vehicleId;
    private String plateNumber;

    private boolean active;
}