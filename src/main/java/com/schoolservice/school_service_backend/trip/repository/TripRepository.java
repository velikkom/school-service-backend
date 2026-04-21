package com.schoolservice.school_service_backend.trip.repository;

import com.schoolservice.school_service_backend.trip.entity.Trip;
import com.schoolservice.school_service_backend.trip.enums.TripStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface TripRepository extends JpaRepository<Trip, UUID> {

    @Query("""
            select t from Trip t
            join fetch t.route r
            join fetch t.vehicle v
            join fetch t.hostess h
            join fetch h.user u
            where h.id = :hostessId
              and t.serviceDate = :serviceDate
              and t.status = :status
              and (
                   (:companyId is null and t.companyId is null)
                or (:companyId is not null and t.companyId = :companyId)
              )
            """)
    Optional<Trip> findActiveTripForHostess(
            @Param("hostessId") UUID hostessId,
            @Param("serviceDate") LocalDate serviceDate,
            @Param("status") TripStatus status,
            @Param("companyId") UUID companyId
    );
}
