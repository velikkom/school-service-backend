package com.schoolservice.school_service_backend.trip.repository;

import com.schoolservice.school_service_backend.trip.entity.TripStudentAttendance;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TripStudentAttendanceRepository extends JpaRepository<TripStudentAttendance, UUID> {

    @EntityGraph(attributePaths = {"student"})
    @Query("select tsa from TripStudentAttendance tsa where tsa.trip.id = :tripId order by tsa.student.lastName, tsa.student.firstName")
    List<TripStudentAttendance> findForTrip(@Param("tripId") UUID tripId);

    Optional<TripStudentAttendance> findByTrip_IdAndStudent_Id(UUID tripId, UUID studentId);
}
