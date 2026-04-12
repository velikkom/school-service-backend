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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;
    private final RouteStopRepository routeStopRepository;
    private final StudentMapper studentMapper;
    private final ParentService parentService;

    /* =========================
       🔥 PARENT METHODS
    ========================= */

    @Override
    public StudentResponse createStudentByEmail(String email, CreateStudentRequest request) {

        Parent parent = parentRepository.findByUser_Email(email)
                .orElseThrow(() -> new EntityNotFoundException("Parent not found"));

        parentService.validateParentActive(parent.getUser().getId());

        Student student = studentMapper.toEntity(request);
        student.setParent(parent);
        System.out.println("STUDENT PARENT ID: " + student.getParent().getId());

        return studentMapper.toResponse(studentRepository.save(student));
    }

    @Override
    public StudentResponse updateStudentByEmail(String email, UUID studentId, CreateStudentRequest request) {

        Parent parent = parentRepository.findByUser_Email(email)
                .orElseThrow(() -> new EntityNotFoundException("Parent not found"));

        parentService.validateParentActive(parent.getUser().getId());

        Student student = getStudentForParent(parent.getId(), studentId);

        studentMapper.updateStudentFromRequest(request, student);

        return studentMapper.toResponse(studentRepository.save(student));
    }

    @Override
    public void deleteStudentByEmail(String email, UUID studentId) {

        Parent parent = parentRepository.findByUser_Email(email)
                .orElseThrow(() -> new EntityNotFoundException("Parent not found"));

        parentService.validateParentActive(parent.getUser().getId());

        Student student = getStudentForParent(parent.getId(), studentId);

        student.deactivate();

        studentRepository.save(student);
    }

    @Override
    public StudentResponse getStudentByEmail(String email, UUID studentId) {

        Parent parent = parentRepository.findByUser_Email(email)
                .orElseThrow(() -> new EntityNotFoundException("Parent not found"));

        Student student = getStudentForParent(parent.getId(), studentId);

        return studentMapper.toResponse(student);
    }

    /* =========================
       🔥 CRITICAL SECURITY METHOD
    ========================= */

    private Student getStudentForParent(UUID parentId, UUID studentId) {

        Student student = studentRepository.findByIdAndActiveTrue(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        if (!student.getParent().getId().equals(parentId)) {
            throw new AccessDeniedException("You cannot access this student");
        }

        return student;
    }

    /* =========================
       🔥 ADMIN METHODS
    ========================= */

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
                studentRepository.findByIdAndActiveTrue(studentId)
                        .orElseThrow(() -> new EntityNotFoundException("Student not found"))
        );
    }

    @Override
    public StudentResponse updateStudent(UUID studentId, CreateStudentRequest request) {

        Student student = studentRepository.findByIdAndActiveTrue(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        studentMapper.updateStudentFromRequest(request, student);

        return studentMapper.toResponse(studentRepository.save(student));
    }

    @Override
    public StudentResponse assignRoute(UUID studentId, UUID routeStopId) {

        Student student = studentRepository.findByIdAndActiveTrue(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        RouteStop routeStop = routeStopRepository.findById(routeStopId)
                .orElseThrow(() -> new EntityNotFoundException("RouteStop not found"));

        student.setRouteStop(routeStop);

        return studentMapper.toResponse(studentRepository.save(student));
    }

    @Override
    public void deleteStudent(UUID studentId) {

        Student student = studentRepository.findByIdAndActiveTrue(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        student.deactivate();

        studentRepository.save(student);
    }

    @Override
    public List<StudentResponse> getStudentsByEmail(String email) {

        Parent parent = parentRepository.findByUser_Email(email)
                .orElseThrow(() -> new EntityNotFoundException("Parent not found"));

        return studentRepository
                .findAllByParentIdAndActiveTrue(parent.getId())
                .stream()
                .map(studentMapper::toResponse)
                .toList();
    }
}