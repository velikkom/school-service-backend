package com.schoolservice.school_service_backend.parent.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateParentRequest {

    @Size(max = 15, message = "phoneNumber must be at most 15 characters")
    private String phoneNumber;

    @Size(max = 255, message = "emergencyContactName must be at most 255 characters")
    private String emergencyContactName;

    @Size(max = 50, message = "emergencyContactPhone must be at most 50 characters")
    private String emergencyContactPhone;

    @Size(max = 500, message = "address must be at most 500 characters")
    private String address;

    @Size(max = 100, message = "district must be at most 100 characters")
    private String district;

    @Size(max = 100, message = "city must be at most 100 characters")
    private String city;

    @Size(max = 50, message = "identityNumber must be at most 50 characters")
    private String identityNumber;
}
