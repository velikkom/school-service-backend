package com.schoolservice.school_service_backend.student.service.impl;

import com.schoolservice.school_service_backend.parent.entity.Parent;
import com.schoolservice.school_service_backend.parent.repository.ParentRepository;
import com.schoolservice.school_service_backend.parent.service.ParentService;
import com.schoolservice.school_service_backend.route.entity.RouteStop;

import com.schoolservice.school_service_backend.route.repository.RouteStopRepository;
import com.schoolservice.school_service_backend.student.dto.request.AdminCreateStudentRequest;
import com.schoolservice.school_service_backend.student.dto.request.CreateStudentRequest;
import com.schoolservice.school_service_backend.student.dto.response.StudentResponse;
import com.schoolservice.school_service_backend.student.entity.Student;
import com.schoolservice.school_service_backend.student.mapper.StudentMapper;
import com.schoolservice.school_service_backend.student.repository.StudentRepository;
import com.schoolservice.school_service_backend.student.service.StudentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;
    private final RouteStopRepository routeStopRepository;
    private final StudentMapper studentMapper;
    private final ParentService parentService;

    // =========================
    // 🔥 PARENT METHODS
    // =========================

    @Override
    public StudentResponse createStudent(UUID userId, CreateStudentRequest request) {

        parentService.validateParentActive(userId);

        Parent parent = parentRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Parent not found"));

        Student student = studentMapper.toEntity(request);
        student.setParent(parent);

        return studentMapper.toResponse(studentRepository.save(student));
    }

    @Override
    public StudentResponse updateStudent(UUID userId, UUID studentId, CreateStudentRequest request) {

        parentService.validateParentActive(userId);

        Student student = getStudentForParent(userId, studentId);

        studentMapper.updateStudentFromRequest(request, student);

        return studentMapper.toResponse(studentRepository.save(student));
    }

    @Override
    public void deleteStudent(UUID userId, UUID studentId) {

        parentService.validateParentActive(userId);

        Student student = getStudentForParent(userId, studentId);

        student.deactivate();

        studentRepository.save(student);
    }

    @Override
    public StudentResponse getStudentById(UUID userId, UUID studentId) {

        Student student = getStudentForParent(userId, studentId);

        return studentMapper.toResponse(student);
    }

    // 🔥 CRITICAL SECURITY METHOD
    private Student getStudentForParent(UUID userId, UUID studentId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        Parent parent = parentRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Parent not found"));

        if (!student.getParent().getId().equals(parent.getId())) {
            throw new RuntimeException("ACCESS_DENIED");
        }

        return student;
    }

    // =========================
    // 🔥 ADMIN METHODS
    // =========================

    @Override
    public StudentResponse adminCreateStudent(AdminCreateStudentRequest request) {

        Parent parent = parentRepository.findById(request.getParentId())
                .orElseThrow(() -> new EntityNotFoundException("Parent not found"));

        Student student = studentMapper.toEntity(request);
        student.setParent(parent);

        return studentMapper.toResponse(studentRepository.save(student));
    }

    @Override
    public Page<Student> getAllStudents(Pageable pageable) {
        return studentRepository.findAllByActiveTrue(pageable);
    }

    @Override
    public StudentResponse getStudentById(UUID studentId) {
        return studentMapper.toResponse(
                studentRepository.findById(studentId)
                        .orElseThrow(() -> new EntityNotFoundException("Student not found"))
        );
    }

    @Override
    public StudentResponse updateStudent(UUID studentId, CreateStudentRequest request) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        studentMapper.updateStudentFromRequest(request, student);

        return studentMapper.toResponse(studentRepository.save(student));
    }

    @Override
    public StudentResponse assignRoute(UUID studentId, UUID routeStopId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        RouteStop routeStop = routeStopRepository.findById(routeStopId)
                .orElseThrow(() -> new EntityNotFoundException("RouteStop not found"));

        student.setRouteStop(routeStop);

        return studentMapper.toResponse(studentRepository.save(student));
    }

    @Override
    public void deleteStudent(UUID studentId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        student.deactivate();

        studentRepository.save(student);
    }
}