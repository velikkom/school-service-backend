package com.schoolservice.school_service_backend.student.service;

import com.schoolservice.school_service_backend.student.dto.request.CreateStudentRequest;
import com.schoolservice.school_service_backend.student.dto.response.StudentResponse;
import com.schoolservice.school_service_backend.student.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface StudentService {

    StudentResponse createStudent(CreateStudentRequest request);

    //Page<StudentResponse> getAllStudents(Pageable pageable);

    StudentResponse getStudentById(UUID studentId);

    StudentResponse updateStudent(UUID studentId, CreateStudentRequest request);

    StudentResponse assignRoute(UUID studentId, UUID routeStopId);

    void deleteStudent(UUID studentId);

    Page<Student> getAllStudents(Pageable pageable);
}
