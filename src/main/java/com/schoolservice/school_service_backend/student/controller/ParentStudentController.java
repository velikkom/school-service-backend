package com.schoolservice.school_service_backend.student.controller;

import com.schoolservice.school_service_backend.student.dto.request.CreateStudentRequest;
import com.schoolservice.school_service_backend.student.dto.response.StudentResponse;
import com.schoolservice.school_service_backend.student.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/parent/students")
@RequiredArgsConstructor
@PreAuthorize("hasRole('PARENT')")
public class ParentStudentController {

    private final StudentService studentService;

    @PostMapping
    @Operation(summary = "Create student")
    public StudentResponse createStudent(
            @AuthenticationPrincipal UUID userId,
            @Valid @RequestBody CreateStudentRequest request
    ) {
        return studentService.createStudent(userId, request);
    }

    @PutMapping("/{studentId}")
    @Operation(summary = "Update student")
    public StudentResponse updateStudent(
            @AuthenticationPrincipal UUID userId,
            @PathVariable UUID studentId,
            @Valid @RequestBody CreateStudentRequest request
    ) {
        return studentService.updateStudent(userId, studentId, request);
    }

    @DeleteMapping("/{studentId}")
    @Operation(summary = "Delete student")
    public void deleteStudent(
            @AuthenticationPrincipal UUID userId,
            @PathVariable UUID studentId
    ) {
        studentService.deleteStudent(userId, studentId);
    }

    @GetMapping("/{studentId}")
    @Operation(summary = "Get student by ID")
    public StudentResponse getStudent(
            @AuthenticationPrincipal UUID userId,
            @PathVariable UUID studentId
    ) {
        return studentService.getStudentById(userId, studentId);
    }
}