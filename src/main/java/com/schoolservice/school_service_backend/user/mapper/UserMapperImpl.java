package com.schoolservice.school_service_backend.user.mapper;

import com.schoolservice.school_service_backend.user.dto.AdminUserResponse;
import com.schoolservice.school_service_backend.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {

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