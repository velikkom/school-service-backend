package com.schoolservice.school_service_backend.hostess.service.ımpl;

import com.schoolservice.school_service_backend.hostess.dto.request.ChangeHostessStatusRequest;
import com.schoolservice.school_service_backend.hostess.dto.request.CreateHostessRequest;
import com.schoolservice.school_service_backend.hostess.dto.request.UpdateHostessRequest;
import com.schoolservice.school_service_backend.hostess.dto.response.HostessResponse;
import com.schoolservice.school_service_backend.hostess.entity.Hostess;
import com.schoolservice.school_service_backend.hostess.mapper.HostessMapper;
import com.schoolservice.school_service_backend.hostess.repository.HostessRepository;
import com.schoolservice.school_service_backend.hostess.service.HostessService;
import com.schoolservice.school_service_backend.user.entity.User;
import com.schoolservice.school_service_backend.user.enums.RoleType;
import com.schoolservice.school_service_backend.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HostessServiceImpl implements HostessService {

    private final HostessRepository hostessRepository;
    private final UserRepository userRepository;

    @Override
    public HostessResponse createHostess(CreateHostessRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + request.getUserId()));

        if (!user.getRoles().contains(RoleType.ROLE_HOSTESS)) {
            throw new IllegalArgumentException("User does not have ROLE_HOSTESS");
        }

        if (hostessRepository.existsByUserId(user.getId())) {
            throw new IllegalArgumentException("Hostess profile already exists for this user");
        }

        Hostess hostess = Hostess.builder()
                .user(user)
                .phone(request.getPhoneNumber())
                .emergencyContact(request.getEmergencyContactName())
                .phone(request.getEmergencyContactPhone())
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        Hostess savedHostess = hostessRepository.save(hostess);

        return HostessMapper.toHostessResponse(savedHostess);
    }

    @Override
    public Page<HostessResponse> getAllHostesses(Pageable pageable) {
        return hostessRepository.findAll(pageable)
                .map(HostessMapper::toHostessResponse);
    }


    @Override
    public HostessResponse getHostessById(UUID hostessId) {
        Hostess hostess = hostessRepository.findById(hostessId)
                .orElseThrow(() -> new EntityNotFoundException("Hostess not found with id: " + hostessId));

        return HostessMapper.toHostessResponse(hostess);
    }

    @Override
    public HostessResponse updateHostess(UUID hostessId, UpdateHostessRequest request) {
        Hostess hostess = hostessRepository.findById(hostessId)
                .orElseThrow(() -> new EntityNotFoundException("Hostess not found with id: " + hostessId));

        hostess.setPhone(request.getPhoneNumber());
        hostess.setEmergencyContact(request.getEmergencyContactName());
        hostess.setUpdatedAt(LocalDateTime.now());

        Hostess updatedHostess = hostessRepository.save(hostess);

        return HostessMapper.toHostessResponse(updatedHostess);
    }

    @Override
    public HostessResponse changeHostessStatus(UUID hostessId, ChangeHostessStatusRequest request) {
        Hostess hostess = hostessRepository.findById(hostessId)
                .orElseThrow(() -> new EntityNotFoundException("Hostess not found with id: " + hostessId));

        hostess.setActive(request.getActive());
        hostess.setUpdatedAt(LocalDateTime.now());

        Hostess updatedHostess = hostessRepository.save(hostess);

        return HostessMapper.toHostessResponse(updatedHostess);
    }

    @Override
    public HostessResponse getMyProfile(UUID userId) {
        Hostess hostess = hostessRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Hostess profile not found for user id: " + userId));

        return HostessMapper.toHostessResponse(hostess);
    }
}