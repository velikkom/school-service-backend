package com.schoolservice.school_service_backend.trip.repository;

import com.schoolservice.school_service_backend.trip.entity.TripStopProgress;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TripStopProgressRepository extends JpaRepository<TripStopProgress, UUID> {

    @EntityGraph(attributePaths = {"routeStop", "routeStop.student"})
    @Query("""
            select tsp from TripStopProgress tsp
            join tsp.routeStop rs
            where tsp.trip.id = :tripId
            order by rs.stopOrder asc
            """)
    List<TripStopProgress> findAllForTripOrdered(@Param("tripId") UUID tripId);

    Optional<TripStopProgress> findByTrip_IdAndRouteStop_Id(UUID tripId, UUID routeStopId);
}
