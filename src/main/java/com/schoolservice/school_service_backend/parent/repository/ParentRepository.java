package com.schoolservice.school_service_backend.parent.repository;

import com.schoolservice.school_service_backend.parent.entity.Parent;
import com.schoolservice.school_service_backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ParentRepository extends JpaRepository<Parent, UUID> {

    Optional<Parent> findByUser(User user);

    Optional<Parent> findByUserId(UUID userId);

    Optional<Parent> findByUser_Email(String email);
}
