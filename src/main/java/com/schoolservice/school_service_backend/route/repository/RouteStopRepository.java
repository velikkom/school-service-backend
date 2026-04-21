package com.schoolservice.school_service_backend.route.repository;

import com.schoolservice.school_service_backend.route.entity.RouteStop;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RouteStopRepository extends JpaRepository<RouteStop, UUID> {

    @EntityGraph(attributePaths = {"student", "route"})
    @Query("select rs from RouteStop rs where rs.route.id = :routeId and rs.active = true order by rs.stopOrder asc")
    List<RouteStop> findWithRouteAndStudentByRouteId(@Param("routeId") UUID routeId);

    @EntityGraph(attributePaths = {"student", "route"})
    @Query("select rs from RouteStop rs where rs.id = :id and rs.route.id = :routeId and rs.active = true")
    Optional<RouteStop> findByIdAndRoute_Id(@Param("id") UUID id, @Param("routeId") UUID routeId);
}
