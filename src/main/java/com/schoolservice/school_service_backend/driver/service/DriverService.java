package com.schoolservice.school_service_backend.driver.service;

import com.schoolservice.school_service_backend.driver.dto.request.CreateDriverRequest;
import com.schoolservice.school_service_backend.driver.dto.response.DriverResponse;

import java.util.List;
import java.util.UUID;

public interface DriverService {

    DriverResponse createDriver(CreateDriverRequest request);

    DriverResponse assignVehicle(UUID driverId, UUID vehicleId);

    DriverResponse removeVehicle(UUID driverId);

    DriverResponse getDriver(UUID driverId);

    List<DriverResponse> getAllDrivers();

    void deactivateDriver(UUID driverId);

    void activateDriver(UUID driverId);
}