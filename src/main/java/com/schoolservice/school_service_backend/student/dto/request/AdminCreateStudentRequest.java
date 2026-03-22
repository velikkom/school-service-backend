package com.schoolservice.school_service_backend.student.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AdminCreateStudentRequest extends CreateStudentRequest {

    @NotNull
    private UUID parentId;
}