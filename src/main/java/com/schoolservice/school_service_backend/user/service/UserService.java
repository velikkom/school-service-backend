package com.schoolservice.school_service_backend.user.service;

import com.schoolservice.school_service_backend.user.dto.*;
import com.schoolservice.school_service_backend.user.enums.RoleType;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


public interface UserService {

    List<PendingUserResponse> getPendingUsers();

    void approveUser(UUID userId, RoleType roleToAssign);

    void rejectUser(UUID userId);

    List<AdminUserResponse>getApprovedUsers();

    void activateUser(UUID userId);

    void deactivateUser(UUID userId);

    AdminUserResponse getUserDetailForAdmin(UUID userId);


   // Page<AdminUserResponse> getUsersForAdmin(AdminUserFilterRequest filter, int page, int size);

    Page<AdminUserResponse> getAllUsersForAdmin(AdminUserFilterRequest filter, int page, int size, String sortBy, String sortDirection);

    void restoreUser(UUID userId);

    void registerUser(CreateUserRequest request);

    void updateMyProfile(String currentEmail, UpdateProfileRequest request);

}
