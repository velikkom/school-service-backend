package com.schoolservice.school_service_backend.hostess.repository;

import com.schoolservice.school_service_backend.hostess.entity.HostessAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public interface HostessAssignmentRepository extends JpaRepository<HostessAssignment, UUID> {

    List<HostessAssignment> findByHostessId(UUID hostessId);

    List<HostessAssignment> findByHostessUserId(UUID userId);

    List<HostessAssignment> findByHostessIdAndActiveTrue(UUID hostessId);

    @Query("""
        select ha
        from HostessAssignment ha
        where ha.hostess.id = :hostessId
          and ha.dayOfWeek = :dayOfWeek
          and ha.active = true
          and (:excludeAssignmentId is null or ha.id <> :excludeAssignmentId)
          and ha.startTime < :endTime
          and ha.endTime > :startTime
    """)
    List<HostessAssignment> findConflictingAssignments(
            UUID hostessId,
            DayOfWeek dayOfWeek,
            LocalTime startTime,
            LocalTime endTime,
            UUID excludeAssignmentId
    );
}