package com.schoolservice.school_service_backend.driver.service.ımpl;

import com.schoolservice.school_service_backend.driver.dto.request.CreateDriverRequest;
import com.schoolservice.school_service_backend.driver.dto.response.DriverResponse;
import com.schoolservice.school_service_backend.driver.entity.Driver;
import com.schoolservice.school_service_backend.driver.mapper.DriverMapper;
import com.schoolservice.school_service_backend.driver.repository.DriverRepository;
import com.schoolservice.school_service_backend.driver.service.DriverService;
import com.schoolservice.school_service_backend.user.entity.User;
import com.schoolservice.school_service_backend.user.repository.UserRepository;
import com.schoolservice.school_service_backend.vehicle.entity.Vehicle;
import com.schoolservice.school_service_backend.vehicle.repository.VehicleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final DriverMapper driverMapper;

    @Override
    public DriverResponse createDriver(CreateDriverRequest request) {

        if (driverRepository.existsByLicenseNumber(request.getLicenseNumber())) {
            throw new RuntimeException("LICENSE_ALREADY_EXISTS");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Driver driver = driverMapper.toEntity(request);
        driver.setUser(user);

        return driverMapper.toResponse(driverRepository.save(driver));
    }

    @Override
    public DriverResponse assignVehicle(UUID driverId, UUID vehicleId) {

        Driver driver = getDriverOrThrow(driverId);
        Vehicle vehicle = getVehicleOrThrow(vehicleId);

        // 🔥 BUSINESS RULES
        if (!driver.isActive()) {
            throw new RuntimeException("DRIVER_NOT_ACTIVE");
        }

        if (!vehicle.isActive()) {
            throw new RuntimeException("VEHICLE_NOT_ACTIVE");
        }

        // 🔥 vehicle zaten atanmış mı?
        if (driverRepository.existsByVehicleId(vehicleId)) {
            throw new RuntimeException("VEHICLE_ALREADY_ASSIGNED");
        }

        driver.assignVehicle(vehicle);

        return driverMapper.toResponse(driver);
    }

    @Override
    public DriverResponse removeVehicle(UUID driverId) {

        Driver driver = getDriverOrThrow(driverId);

        driver.removeVehicle();

        return driverMapper.toResponse(driver);
    }

    @Override
    public DriverResponse getDriver(UUID driverId) {
        return driverMapper.toResponse(getDriverOrThrow(driverId));
    }

    @Override
    public List<DriverResponse> getAllDrivers() {
        return driverRepository.findAll()
                .stream()
                .map(driverMapper::toResponse)
                .toList();
    }

    @Override
    public void deactivateDriver(UUID driverId) {
        getDriverOrThrow(driverId).deactivate();
    }

    @Override
    public void activateDriver(UUID driverId) {
        getDriverOrThrow(driverId).activate();
    }

    // =========================
    // 🔥 HELPERS
    // =========================

    private Driver getDriverOrThrow(UUID id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DRIVER_NOT_FOUND"));
    }

    private Vehicle getVehicleOrThrow(UUID id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("VEHICLE_NOT_FOUND"));
    }
}