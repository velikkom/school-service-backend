package com.schoolservice.school_service_backend.student.service;

import com.schoolservice.school_service_backend.student.dto.request.AdminCreateStudentRequest;
import com.schoolservice.school_service_backend.student.dto.request.CreateStudentRequest;
import com.schoolservice.school_service_backend.student.dto.response.StudentResponse;
import com.schoolservice.school_service_backend.student.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface StudentService {

    // parent
    StudentResponse createStudent(UUID userId, CreateStudentRequest request);
    StudentResponse updateStudent(UUID userId, UUID studentId, CreateStudentRequest request);
    void deleteStudent(UUID userId, UUID studentId);
    StudentResponse getStudentById(UUID userId, UUID studentId);

    // admin
    StudentResponse adminCreateStudent(AdminCreateStudentRequest request);

    Page<Student> getAllStudents(Pageable pageable);
    StudentResponse getStudentById(UUID studentId);
    StudentResponse updateStudent(UUID studentId, CreateStudentRequest request);
    StudentResponse assignRoute(UUID studentId, UUID routeStopId);
    void deleteStudent(UUID studentId);
}
