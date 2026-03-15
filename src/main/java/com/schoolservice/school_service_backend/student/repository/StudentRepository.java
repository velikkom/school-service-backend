package com.schoolservice.school_service_backend.student.repository;

import com.schoolservice.school_service_backend.student.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID> {

    Page<Student> findAllByActiveTrue(Pageable pageable);
}
