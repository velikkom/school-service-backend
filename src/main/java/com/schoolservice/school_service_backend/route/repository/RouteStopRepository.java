package com.schoolservice.school_service_backend.route.repository;

import com.schoolservice.school_service_backend.route.entity.RouteStop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RouteStopRepository extends JpaRepository<RouteStop, UUID> {

}
