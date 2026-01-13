package com.schoolservice.school_service_backend.user.service.impl;

import com.schoolservice.school_service_backend.user.dto.PendingUserResponse;
import com.schoolservice.school_service_backend.user.entity.User;
import com.schoolservice.school_service_backend.user.enums.ApprovalStatus;
import com.schoolservice.school_service_backend.user.repository.UserRepository;
import com.schoolservice.school_service_backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

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

}
