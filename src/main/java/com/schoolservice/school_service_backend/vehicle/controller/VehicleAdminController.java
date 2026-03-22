package com.schoolservice.school_service_backend.vehicle.controller;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.schoolservice.school_service_backend.vehicle.dto.request.CreateVehicleRequest;
import com.schoolservice.school_service_backend.vehicle.dto.request.UpdateVehicleRequest;
import com.schoolservice.school_service_backend.vehicle.dto.response.VehicleResponse;
import com.schoolservice.school_service_backend.vehicle.entity.Vehicle;
import com.schoolservice.school_service_backend.vehicle.mapper.VehicleMapper;
import com.schoolservice.school_service_backend.vehicle.repository.VehicleRepository;
import com.schoolservice.school_service_backend.vehicle.service.VehicleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/vehicles")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class VehicleAdminController {

    private final VehicleService vehicleService;

    @PostMapping
    public VehicleResponse create(@Valid @RequestBody CreateVehicleRequest request) {
        return vehicleService.createVehicle(request);
    }

    @PutMapping("/{id}")
    public VehicleResponse update(
            @PathVariable UUID id,
            @RequestBody UpdateVehicleRequest request
    ) {
        return vehicleService.updateVehicle(id, request);
    }

    @GetMapping("/{id}")
    public VehicleResponse get(@PathVariable UUID id) {
        return vehicleService.getVehicle(id);
    }

    @GetMapping
    public List<VehicleResponse> getAll() {
        return vehicleService.getAllVehicles();
    }

    @PatchMapping("/{id}/activate")
    public void activate(@PathVariable UUID id) {
        vehicleService.activateVehicle(id);
    }

    @PatchMapping("/{id}/deactivate")
    public void deactivate(@PathVariable UUID id) {
        vehicleService.deactivateVehicle(id);
    }
}