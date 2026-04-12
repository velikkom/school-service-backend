package com.schoolservice.school_service_backend.student.service;

import com.schoolservice.school_service_backend.student.dto.request.AdminCreateStudentRequest;
import com.schoolservice.school_service_backend.student.dto.request.CreateStudentRequest;
import com.schoolservice.school_service_backend.student.dto.response.StudentResponse;
import com.schoolservice.school_service_backend.student.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface StudentService {

    /* =========================
       🔥 PARENT METHODS
    ========================= */

    StudentResponse createStudentByEmail(String email, CreateStudentRequest request);

    StudentResponse updateStudentByEmail(String email, UUID studentId, CreateStudentRequest request);

    void deleteStudentByEmail(String email, UUID studentId);

    StudentResponse getStudentByEmail(String email, UUID studentId);


    /* =========================
       🔥 ADMIN METHODS
    ========================= */

    StudentResponse adminCreateStudent(AdminCreateStudentRequest request);

    Page<Student> getAllStudents(Pageable pageable);

    StudentResponse getStudentById(UUID studentId);

    StudentResponse updateStudent(UUID studentId, CreateStudentRequest request);

    StudentResponse assignRoute(UUID studentId, UUID routeStopId);

    void deleteStudent(UUID studentId);

    List<StudentResponse> getStudentsByEmail(String email);
}