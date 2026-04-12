package com.schoolservice.school_service_backend.student.repository;

import com.schoolservice.school_service_backend.student.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID> {

    /* =========================
       🔥 BASIC ACTIVE FILTER
    ========================= */

    Optional<Student> findByIdAndActiveTrue(UUID id);

    Page<Student> findAllByActiveTrue(Pageable pageable);


    /* =========================
       🔥 PARENT BASED QUERIES
    ========================= */

    List<Student> findAllByParent_IdAndActiveTrue(UUID parentId);

    Page<Student> findAllByParent_IdAndActiveTrue(UUID parentId, Pageable pageable);


    /* =========================
       🔥 EXISTS (VALIDATION)
    ========================= */

    boolean existsByParent_IdAndFirstNameAndLastNameAndBirthDate(
            UUID parentId,
            String firstName,
            String lastName,
            java.time.LocalDate birthDate
    );


    /* =========================
       🔥 FETCH JOIN (PERFORMANCE)
    ========================= */

    @org.springframework.data.jpa.repository.Query("""
        SELECT s FROM Student s
        JOIN FETCH s.parent
        WHERE s.id = :id AND s.active = true
    """)
    Optional<Student> findByIdWithParent(UUID id);


    /* =========================
       🔥 OPTIONAL (ROUTE FILTER)
    ========================= */

    List<Student> findAllByRouteStop_IdAndActiveTrue(UUID routeStopId);

    List<Student> findAllByParentIdAndActiveTrue(UUID id);
}