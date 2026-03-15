package com.schoolservice.school_service_backend.student.dto.response;

import com.schoolservice.school_service_backend.student.enums.Gender;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResponse {

    private UUID id;

    private String firstName;
    private String lastName;

    private LocalDate birthDate;
    private Gender gender;

    private String schoolName;
    private String grade;
    private String className;

    private String address;
    private String district;
    private String city;

    private UUID parentId;
    private String parentName;

    private UUID routeStopId;
    private String routeName;

    private boolean active;
}