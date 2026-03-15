package com.schoolservice.school_service_backend.vehicle.repository;

import com.schoolservice.school_service_backend.vehicle.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {
}
