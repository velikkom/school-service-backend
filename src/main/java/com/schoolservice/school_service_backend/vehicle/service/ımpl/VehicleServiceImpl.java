package com.schoolservice.school_service_backend.vehicle.service.ımpl;

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

@Service
@RequiredArgsConstructor
@Transactional
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;

    @Override
    public VehicleResponse createVehicle(CreateVehicleRequest request) {

        // 🔥 BUSINESS RULE
        if (vehicleRepository.existsByPlateNumber(request.getPlateNumber())) {
            throw new RuntimeException("PLATE_ALREADY_EXISTS");
        }

        Vehicle vehicle = vehicleMapper.toEntity(request);

        return vehicleMapper.toResponse(
                vehicleRepository.save(vehicle)
        );
    }

    @Override
    public VehicleResponse updateVehicle(UUID id, UpdateVehicleRequest request) {

        Vehicle vehicle = getVehicleOrThrow(id);

        vehicle.updateDetails(
                request.getBrand(),
                request.getModel(),
                request.getCapacity()
        );

        return vehicleMapper.toResponse(vehicle);
    }

    @Override
    public VehicleResponse getVehicle(UUID id) {
        return vehicleMapper.toResponse(getVehicleOrThrow(id));
    }

    @Override
    public List<VehicleResponse> getAllVehicles() {
        return vehicleRepository.findAll()
                .stream()
                .map(vehicleMapper::toResponse)
                .toList();
    }

    @Override
    public void deactivateVehicle(UUID id) {
        Vehicle vehicle = getVehicleOrThrow(id);
        vehicle.deactivate();
    }

    @Override
    public void activateVehicle(UUID id) {
        Vehicle vehicle = getVehicleOrThrow(id);
        vehicle.activate();
    }

    // =========================
    // 🔥 PRIVATE HELPERS
    // =========================

    private Vehicle getVehicleOrThrow(UUID id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("VEHICLE_NOT_FOUND"));
    }
}
