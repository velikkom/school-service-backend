package com.schoolservice.school_service_backend.parent.service.impl;

import com.schoolservice.school_service_backend.common.exception.BusinessException;
import com.schoolservice.school_service_backend.common.exception.ProfileNotCompletedException;
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

    @Override
    public void validateParentActive(UUID userId) {

        Parent parent = getParentOrThrow(userId);

        validateUserApproved(parent);
        validateUserActive(parent);
        validateProfileCompleted(parent);
    }

    @Override
    public ParentResponse getMyProfileByEmail(String email) {

        Parent parent = parentRepository.findByUser_Email(email)
                .orElseThrow(() -> new ResourceNotFoundException("Parent not found"));

        return parentMapper.toResponse(parent);
    }

    @Override
    public ParentResponse updateProfileByEmail(
            String email,
            UpdateParentRequest request
    ) {
        Parent parent = parentRepository.findByUser_Email(email)
                .orElseThrow(() -> new ResourceNotFoundException("Parent not found"));

        validateUserApproved(parent);
        validateUserActive(parent);

        parentMapper.updateParentFromDto(request, parent);

        parentRepository.save(parent);

        return parentMapper.toResponse(parent);
    }

    private Parent getParentOrThrow(UUID userId) {
        return parentRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Parent not found"));
    }

    private void validateUserApproved(Parent parent) {
        if (parent.getUser().getApprovalStatus() != ApprovalStatus.APPROVED) {
            throw new BusinessException("USER_NOT_APPROVED");
        }
    }

    private void validateUserActive(Parent parent) {
        if (!parent.getUser().isActive()) {
            throw new BusinessException("USER_NOT_ACTIVE");
        }
    }

    private void validateProfileCompleted(Parent parent) {
        if (!parent.isProfileComplete()) {
            throw new ProfileNotCompletedException();
        }
    }
}
