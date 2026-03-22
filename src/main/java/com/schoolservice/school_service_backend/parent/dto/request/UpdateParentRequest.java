package com.schoolservice.school_service_backend.parent.dto.request;

import lombok.Data;

@Data
public class UpdateParentRequest {

    private String phoneNumber;
    private String emergencyContactName;
    private String emergencyContactPhone;

    private String address;
    private String district;
    private String city;

    private String identityNumber;
}