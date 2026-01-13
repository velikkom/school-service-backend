package com.schoolservice.school_service_backend.user.service;

import com.schoolservice.school_service_backend.user.dto.AdminUserResponse;
import com.schoolservice.school_service_backend.user.dto.PendingUserResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


public interface UserService {

    List<PendingUserResponse> getPendingUsers();

    void approveUser(UUID userId);

    void rejectUser(UUID userId);

    List<AdminUserResponse>getApprovedUsers();

    void activateUser(UUID userId);

    void deactivateUser(UUID userId);
}
