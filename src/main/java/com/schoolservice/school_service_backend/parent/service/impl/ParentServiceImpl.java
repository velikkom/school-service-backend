package com.schoolservice.school_service_backend.parent.service.impl;

import com.schoolservice.school_service_backend.common.exception.ResourceNotFoundException;
import com.schoolservice.school_service_backend.parent.dto.request.UpdateParentRequest;
import com.schoolservice.school_service_backend.parent.dto.response.ParentResponse;
import com.schoolservice.school_service_backend.parent.entity.Parent;
import com.schoolservice.school_service_backend.parent.mapper.ParentMapper;
import com.schoolservice.school_service_backend.parent.repository.ParentRepository;
import com.schoolservice.school_service_backend.parent.service.ParentService;
import com.schoolservice.school_service_backend.user.enums.ApprovalStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ParentServiceImpl implements ParentService {

    private final ParentRepository parentRepository;
    private final ParentMapper parentMapper;

    /**
     * Parent profil güncelleme
     * - Sadece admin tarafından onaylanmış kullanıcılar yapabilir
     */
    @Override
    public ParentResponse updateProfile(
            UUID userId,
            UpdateParentRequest request
    ) {

        Parent parent = getParentOrThrow(userId);

        validateUserApproved(parent);
        validateUserActive(parent);

        parentMapper.updateParentFromDto(request, parent);

        parentRepository.save(parent);

        return parentMapper.toResponse(parent);
    }

    /**
     * Parent kendi profilini görüntüler
     */
    @Override
    public ParentResponse getMyProfile(UUID userId) {

        Parent parent = getParentOrThrow(userId);

        return parentMapper.toResponse(parent);
    }

    /**
     * 🔥 SİSTEM ERİŞİM KONTROLÜ
     * Student create vb. işlemlerde kullanılacak
     */
    @Override
    public void validateParentActive(UUID userId) {

        Parent parent = getParentOrThrow(userId);

        validateUserApproved(parent);
        validateUserActive(parent);
        validateProfileCompleted(parent);
    }

    // =========================
    // 🔥 PRIVATE HELPERS (SENIOR LEVEL)
    // =========================

    private Parent getParentOrThrow(UUID userId) {
        return parentRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Parent not found"));
    }

    private void validateUserApproved(Parent parent) {
        if (parent.getUser().getApprovalStatus() != ApprovalStatus.APPROVED) {
            throw new RuntimeException("USER_NOT_APPROVED");
        }
    }

    private void validateUserActive(Parent parent) {
        if (!parent.getUser().isActive()) {
            throw new RuntimeException("USER_NOT_ACTIVE");
        }
    }

    private void validateProfileCompleted(Parent parent) {
        if (!parent.isProfileComplete()) {
            throw new RuntimeException("PROFILE_NOT_COMPLETED");
        }
    }
}