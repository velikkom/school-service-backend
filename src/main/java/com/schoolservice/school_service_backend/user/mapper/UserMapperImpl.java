package com.schoolservice.school_service_backend.user.mapper;

import com.schoolservice.school_service_backend.user.dto.AdminUserResponse;
import com.schoolservice.school_service_backend.user.dto.CreateUserRequest;
import com.schoolservice.school_service_backend.user.entity.User;
import com.schoolservice.school_service_backend.user.enums.ApprovalStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class UserMapperImpl implements UserMapper {

    private final PasswordEncoder passwordEncoder;

    public UserMapperImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User toEntity(CreateUserRequest request) {
        if (request == null){
            return null;
        }

        User user = new User();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());

        user.setRoles(Set.of());
        user.setApprovalStatus(ApprovalStatus.PENDING);
        user.setActive(false);
        user.setDeletedAt(null);

        return user;
    }

    @Override
    public AdminUserResponse toAdminUserResponse(User user) {

        if (user == null) {
            return null;
        }

        return new AdminUserResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRoles(),
                user.isActive(),
                user.getApprovalStatus()
        );
    }
}