package com.schoolservice.school_service_backend.hostess.repository;

import com.schoolservice.school_service_backend.hostess.entity.Hostess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface HostessRepository extends JpaRepository<Hostess, UUID> {

    Optional<Hostess> findByUser_Id(UUID userId);

    boolean existsByUserId(UUID userId);
}
