package com.schoolservice.school_service_backend.parent.service;

import com.schoolservice.school_service_backend.parent.dto.request.UpdateParentRequest;
import com.schoolservice.school_service_backend.parent.dto.response.ParentResponse;

import java.util.UUID;

public interface ParentService {

    ParentResponse updateProfile(
            UUID userId,
            UpdateParentRequest request
    );

    ParentResponse getMyProfile(UUID userId);

    void validateParentActive(UUID userId);
}