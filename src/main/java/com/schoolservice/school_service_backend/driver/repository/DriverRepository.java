package com.schoolservice.school_service_backend.driver.repository;

import com.schoolservice.school_service_backend.driver.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DriverRepository extends JpaRepository<Driver, UUID> {

    boolean existsByLicenseNumber(String licenseNumber);

    boolean existsByVehicleId(UUID vehicleId);
}