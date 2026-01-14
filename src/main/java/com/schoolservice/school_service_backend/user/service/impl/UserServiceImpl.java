package com.schoolservice.school_service_backend.user.service.impl;

import com.schoolservice.school_service_backend.common.audit.AdminAuditService;
import com.schoolservice.school_service_backend.common.audit.AuditAction;
import com.schoolservice.school_service_backend.common.exception.BusinessException;
import com.schoolservice.school_service_backend.common.exception.ResourceNotFoundException;
import com.schoolservice.school_service_backend.user.dto.AdminUserFilterRequest;
import com.schoolservice.school_service_backend.user.dto.AdminUserResponse;
import com.schoolservice.school_service_backend.user.dto.PendingUserResponse;
import com.schoolservice.school_service_backend.user.entity.User;
import com.schoolservice.school_service_backend.user.enums.ApprovalStatus;
import com.schoolservice.school_service_backend.user.mapper.UserMapper;
import com.schoolservice.school_service_backend.user.repository.UserRepository;
import com.schoolservice.school_service_backend.user.service.UserService;
import com.schoolservice.school_service_backend.user.specification.UserSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AdminAuditService auditService;

    private static final String DEFAULT_SORT_BY = "createdAt";
    private static final String DEFAULT_SORT_DIRECTION = "desc";
    private static final int MAX_PAGE_SIZE = 50;

    private boolean isCurrentAdmin(User targetUser) {
        String currentEmail = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return targetUser.getEmail().equals(currentEmail);
    }

    @Override
    public List<PendingUserResponse> getPendingUsers() {

        return userRepository.findByApprovalStatus(ApprovalStatus.PENDING)
                .stream()
                .map(user -> new PendingUserResponse(
                        user.getId(),
                        user.getEmail(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getRoles()
                                .stream()
                                .map(Enum::name)
                                .toList()
                ))
                .toList();
    }

    @Override
    public void approveUser(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (user.getApprovalStatus() == ApprovalStatus.APPROVED) {
            return;
        }

        user.setApprovalStatus(ApprovalStatus.APPROVED);
        userRepository.save(user);
    }

    @Override
    public void rejectUser(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (user.getApprovalStatus() == ApprovalStatus.APPROVED) {
            return;
        }

        user.setApprovalStatus(ApprovalStatus.REJECTED);
        userRepository.save(user);
    }

    @Override
    public List<AdminUserResponse> getApprovedUsers() {
        return userRepository.findByApprovalStatus(ApprovalStatus.APPROVED)
                .stream()
                .map(userMapper::toAdminUserResponse)
                .toList();
    }

    @Override
    public void activateUser(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );

        if (user.isActive()) {
            throw new BusinessException("User is already active");
        }

        if (user.getApprovalStatus() != ApprovalStatus.APPROVED) {
            throw new BusinessException("Only approved users can be activated");
        }

        if (isCurrentAdmin(user)) {
            throw new BusinessException("Admin cannot activate own account");
        }

        user.setActive(true);
        userRepository.save(user);
    }

    @Override
    public void deactivateUser(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );

        if (!user.isActive()) {
            throw new BusinessException("User is already inactive");
        }

        if (isCurrentAdmin(user)) {
            throw new BusinessException("Admin cannot deactivate own account");
        }

        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public AdminUserResponse getUserDetailForAdmin(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return userMapper.toAdminUserResponse(user);
    }

//    @Override
//    public Page<AdminUserResponse> getUsersForAdmin(AdminUserFilterRequest filter, int page, int size) {
//
//        Pageable pageable = PageRequest.of(
//                page,
//                size,
//                Sort.by("createdAt").descending());
//        Page<User> users = userRepository
//                .findAll(UserSpecification.withFilters(filter), pageable);
//
//        return users.map(userMapper::toAdminUserResponse);
//    }

    @Override
    public Page<AdminUserResponse> getAllUsersForAdmin(
            AdminUserFilterRequest filter,
            int page,
            int size,
            String sortBy,
            String sortDirection
    ) {
        int safeSize = Math.min(size, MAX_PAGE_SIZE);

        Sort sort = Sort.by(
                sortDirection.equalsIgnoreCase("asc")
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC,
                resolveSortField(sortBy)
        );

        Pageable pageable = PageRequest.of(page, safeSize, sort);

        Page<User> users = userRepository.findAll(
                UserSpecification.withFilters(filter),
                pageable
        );

        return users.map(userMapper::toAdminUserResponse);
    }

    @Override
    public void restoreUser(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getDeletedAt() == null) {
            throw new BusinessException("User is not deleted");
        }

        user.setDeletedAt(null);
        user.setActive(true);

        userRepository.save(user);
        auditService.log(AuditAction.USER_RESTORED, userId, "User restored by admin");
    }




    /**
     * Whitelist allowed sort fields to prevent invalid / unsafe sorting
     */
    private String resolveSortField(String sortBy) {
        return switch (sortBy) {
            case "email" -> "email";
            case "firstName" -> "firstName";
            case "lastName" -> "lastName";
            case "createdAt" -> "createdAt";
            default -> "createdAt";
        };
    }
}




