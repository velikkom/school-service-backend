package com.schoolservice.school_service_backend.parent.service;

import com.schoolservice.school_service_backend.parent.dto.request.UpdateParentRequest;
import com.schoolservice.school_service_backend.parent.dto.response.ParentResponse;

import java.util.UUID;

public interface ParentService {

    void validateParentActive(UUID userId);

    ParentResponse getMyProfileByEmail(String email);

    ParentResponse updateProfileByEmail(String email, UpdateParentRequest request);
}
