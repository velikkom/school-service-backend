package com.schoolservice.school_service_backend.hostess.service;

import com.schoolservice.school_service_backend.hostess.dto.request.ChangeHostessStatusRequest;
import com.schoolservice.school_service_backend.hostess.dto.request.CreateHostessRequest;
import com.schoolservice.school_service_backend.hostess.dto.request.UpdateHostessRequest;
import com.schoolservice.school_service_backend.hostess.dto.response.HostessResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface HostessService {

    HostessResponse createHostess(CreateHostessRequest request);

    Page<HostessResponse> getAllHostesses(Pageable pageable);

    HostessResponse getHostessById(UUID hostessId);

    HostessResponse updateHostess(UUID hostessId, UpdateHostessRequest request);

    HostessResponse changeHostessStatus(UUID hostessId, ChangeHostessStatusRequest request);

    HostessResponse getMyProfile(UUID userId);
}