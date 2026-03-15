package com.schoolservice.school_service_backend.student.dto.request;

import com.schoolservice.school_service_backend.student.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateStudentRequest {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotNull
    private LocalDate birthDate;

    @NotNull
    private Gender gender;

    private String schoolName;
    private String grade;
    private String className;

    @NotBlank
    private String address;

    private String district;
    private String city;

    @NotNull
    private UUID parentId;

    private UUID routeStopId; // admin set edebilir
}