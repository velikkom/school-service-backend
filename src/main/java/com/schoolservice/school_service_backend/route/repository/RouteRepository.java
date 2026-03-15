package com.schoolservice.school_service_backend.route.repository;

import com.schoolservice.school_service_backend.route.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RouteRepository extends JpaRepository<Route, UUID> {
}
