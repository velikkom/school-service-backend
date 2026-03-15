package com.schoolservice.school_service_backend.hostess.service;

import com.schoolservice.school_service_backend.hostess.dto.request.CreateHostessAssignmentRequest;
import com.schoolservice.school_service_backend.hostess.dto.request.UpdateHostessAssignmentRequest;
import com.schoolservice.school_service_backend.hostess.dto.response.HostessAssignmentResponse;

import java.util.List;
import java.util.UUID;

public interface HostessAssignmentService {

    HostessAssignmentResponse createAssignment(UUID hostessId, CreateHostessAssignmentRequest request);

    List<HostessAssignmentResponse> getAssignmentsByHostessId(UUID hostessId);

    HostessAssignmentResponse getAssignmentById(UUID hostessId, UUID assignmentId);

    HostessAssignmentResponse updateAssignment(
            UUID hostessId,
            UUID assignmentId,
            UpdateHostessAssignmentRequest request
    );

    void deleteAssignment(UUID hostessId, UUID assignmentId);

    List<HostessAssignmentResponse> getMyAssignments(UUID userId);
}