package com.schoolservice.school_service_backend.student.controller;

import com.schoolservice.school_service_backend.common.response.ResponseWrapper;
import com.schoolservice.school_service_backend.student.dto.request.CreateStudentRequest;
import com.schoolservice.school_service_backend.student.dto.response.StudentResponse;
import com.schoolservice.school_service_backend.student.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/parent/students")
@RequiredArgsConstructor
public class ParentStudentController {

    private final StudentService studentService;

    /* =========================
       CREATE STUDENT
    ========================= */
    @PostMapping
    @Operation(summary = "Create student")
    public ResponseEntity<ResponseWrapper<StudentResponse>> createStudent(
            Authentication authentication,
            @Valid @RequestBody CreateStudentRequest request
    ) {

        String email = (String) authentication.getPrincipal();

        StudentResponse response =
                studentService.createStudentByEmail(email, request);

        return ResponseEntity.ok(
                ResponseWrapper.success(response, "Student created successfully")
        );
    }

    /* =========================
       UPDATE STUDENT
    ========================= */
    @PutMapping("/{studentId}")
    @Operation(summary = "Update student")
    public ResponseEntity<ResponseWrapper<StudentResponse>> updateStudent(
            Authentication authentication,
            @PathVariable UUID studentId,
            @Valid @RequestBody CreateStudentRequest request
    ) {

        String email = (String) authentication.getPrincipal();

        StudentResponse response =
                studentService.updateStudentByEmail(email, studentId, request);

        return ResponseEntity.ok(
                ResponseWrapper.success(response, "Student updated successfully")
        );
    }

    /* =========================
       DELETE STUDENT (SOFT DELETE)
    ========================= */
    @DeleteMapping("/{studentId}")
    @Operation(summary = "Delete student")
    public ResponseEntity<ResponseWrapper<Void>> deleteStudent(
            Authentication authentication,
            @PathVariable UUID studentId
    ) {

        String email = (String) authentication.getPrincipal();

        studentService.deleteStudentByEmail(email, studentId);

        return ResponseEntity.ok(
                ResponseWrapper.success(null, "Student deleted successfully")
        );
    }

    /* =========================
       GET STUDENT BY ID
    ========================= */
    @GetMapping("/{studentId}")
    @Operation(summary = "Get student by ID")
    public ResponseEntity<ResponseWrapper<StudentResponse>> getStudent(
            Authentication authentication,
            @PathVariable UUID studentId
    ) {

        String email = (String) authentication.getPrincipal();

        StudentResponse response =
                studentService.getStudentByEmail(email, studentId);

        return ResponseEntity.ok(
                ResponseWrapper.success(response)
        );
    }

    @GetMapping
    @Operation(summary = "Get all student for parent")
    public ResponseEntity<ResponseWrapper<List<StudentResponse>>>getStudents(
            Authentication authentication
    ){
        String email = (String) authentication.getPrincipal();
        List<StudentResponse> students =
                studentService.getStudentsByEmail(email);


        return ResponseEntity.ok(
                ResponseWrapper.success(students)
        );
    }
}