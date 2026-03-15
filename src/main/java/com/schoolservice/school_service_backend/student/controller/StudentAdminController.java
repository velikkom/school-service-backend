package com.schoolservice.school_service_backend.student.controller;

import com.schoolservice.school_service_backend.common.response.PageResponse;
import com.schoolservice.school_service_backend.common.response.ResponseWrapper;
import com.schoolservice.school_service_backend.common.util.PageMapper;
import com.schoolservice.school_service_backend.student.dto.request.CreateStudentRequest;
import com.schoolservice.school_service_backend.student.dto.response.StudentResponse;
import com.schoolservice.school_service_backend.student.entity.Student;
import com.schoolservice.school_service_backend.student.mapper.StudentMapper;
import com.schoolservice.school_service_backend.student.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/students")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class StudentAdminController {

    private final StudentService studentService;
    private final StudentMapper studentMapper;

    @PostMapping
    @Operation(summary = "Create student")
    public ResponseEntity<ResponseWrapper<StudentResponse>> createStudent(
            @Valid @RequestBody CreateStudentRequest request
    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseWrapper.success(
                        studentService.createStudent(request),
                        "Student created successfully"
                ));
    }

    @GetMapping
    @Operation(summary = "Get all students (paginated)")
    public ResponseEntity<ResponseWrapper<PageResponse<StudentResponse>>> getAllStudents(
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable
    ) {
        Page<Student> page = studentService.getAllStudents(pageable);

        return buildPageResponse(page, studentMapper::toResponse);


    }

    @GetMapping("/{studentId}")
    public ResponseEntity<ResponseWrapper<StudentResponse>> getStudentById(
            @PathVariable UUID studentId
    ) {

        return ResponseEntity.ok(
                ResponseWrapper.success(
                        studentService.getStudentById(studentId)
                )
        );
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<ResponseWrapper<StudentResponse>> updateStudent(
            @PathVariable UUID studentId,
            @Valid @RequestBody CreateStudentRequest request
    ) {

        return ResponseEntity.ok(
                ResponseWrapper.success(
                        studentService.updateStudent(studentId, request),
                        "Student updated successfully"
                )
        );
    }

    @PatchMapping("/{studentId}/route/{routeStopId}")
    public ResponseEntity<ResponseWrapper<StudentResponse>> assignRoute(
            @PathVariable UUID studentId,
            @PathVariable UUID routeStopId
    ) {

        return ResponseEntity.ok(
                ResponseWrapper.success(
                        studentService.assignRoute(studentId, routeStopId),
                        "Route assigned successfully"
                )
        );
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<ResponseWrapper<Void>> deleteStudent(
            @PathVariable UUID studentId
    ) {

        studentService.deleteStudent(studentId);

        return ResponseEntity.ok(
                ResponseWrapper.success(null, "Student deleted successfully")
        );
    }

    /*******************************HELPER METHODS*******************************/
    private <T, R> ResponseEntity<ResponseWrapper<PageResponse<R>>> buildPageResponse(
            Page<T> page,
            java.util.function.Function<T, R> mapper
    ) {

        PageResponse<R> pageResponse =
                PageMapper.toPageResponse(page, mapper);

        return ResponseEntity.ok(
                ResponseWrapper.success(pageResponse)
        );
    }
}