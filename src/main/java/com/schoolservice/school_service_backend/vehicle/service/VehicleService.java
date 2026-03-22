package com.schoolservice.school_service_backend.vehicle.service;

import com.schoolservice.school_service_backend.vehicle.dto.request.CreateVehicleRequest;
import com.schoolservice.school_service_backend.vehicle.dto.request.UpdateVehicleRequest;
import com.schoolservice.school_service_backend.vehicle.dto.response.CreateVehicleResponse;
import com.schoolservice.school_service_backend.vehicle.dto.response.VehicleResponse;

import java.util.List;
import java.util.UUID;

public interface VehicleService {

    VehicleResponse createVehicle(CreateVehicleRequest request);

    VehicleResponse updateVehicle(UUID id, UpdateVehicleRequest request);

    VehicleResponse getVehicle(UUID id);

    List<VehicleResponse> getAllVehicles();

    void deactivateVehicle(UUID id);

    void activateVehicle(UUID id);
}