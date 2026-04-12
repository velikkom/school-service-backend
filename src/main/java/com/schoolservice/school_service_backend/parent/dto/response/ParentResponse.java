package com.schoolservice.school_service_backend.parent.dto.response;

import com.schoolservice.school_service_backend.parent.entity.ProfileStatus;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;


@Data
@Builder
public class ParentResponse {

    private UUID id;

    // 🔥 USER INFO (flattened)
    private String firstName;
    private String lastName;
    private String email;

    // 🔥 PARENT INFO
    private String phoneNumber;
    private String emergencyContactName;
    private String emergencyContactPhone;

    private String address;
    private String district;
    private String city;

    private String identityNumber;

    private ProfileStatus profileStatus;

}

