package com.schoolservice.school_service_backend.driver.controller;

import com.schoolservice.school_service_backend.driver.dto.request.CreateDriverRequest;
import com.schoolservice.school_service_backend.driver.dto.response.DriverResponse;
import com.schoolservice.school_service_backend.driver.service.DriverService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/drivers")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class DriverAdminController {

    private final DriverService driverService;

    @PostMapping
    @Operation(summary = "Create a new driver")
    public DriverResponse create(@RequestBody CreateDriverRequest request) {
        return driverService.createDriver(request);
    }

    @PatchMapping("/{driverId}/vehicle/{vehicleId}")
    @Operation(summary = "Assign a vehicle to a driver")
    public DriverResponse assignVehicle(
            @PathVariable UUID driverId,
            @PathVariable UUID vehicleId
    ) {
        return driverService.assignVehicle(driverId, vehicleId);
    }

    @PatchMapping("/{driverId}/remove-vehicle")
    @Operation(summary = "Remove a vehicle from a driver")
    public DriverResponse removeVehicle(@PathVariable UUID driverId) {
        return driverService.removeVehicle(driverId);
    }

    @GetMapping("/{driverId}")
    @Operation(summary = "Get a driver by ID")
    public DriverResponse get(@PathVariable UUID driverId) {
        return driverService.getDriver(driverId);
    }

    @GetMapping
    @Operation(summary = "Get all drivers")
    public List<DriverResponse> getAll() {
        return driverService.getAllDrivers();
    }
}