package com.schoolservice.school_service_backend.user.mapper;

import com.schoolservice.school_service_backend.user.dto.AdminUserResponse;
import com.schoolservice.school_service_backend.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public interface UserMapper {

    AdminUserResponse toAdminUserResponse(User user);
}
