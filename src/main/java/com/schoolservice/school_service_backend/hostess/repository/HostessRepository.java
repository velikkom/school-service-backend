package com.schoolservice.school_service_backend.hostess.repository;

import com.schoolservice.school_service_backend.hostess.entity.Hostess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface HostessRepository extends JpaRepository<Hostess, UUID> {

    Optional<Hostess> findByUser_Id(UUID userId);

    boolean existsByUserId(UUID userId);

    @Query("select h from Hostess h join fetch h.user u where u.email = :email")
    Optional<Hostess> findByUser_Email(@Param("email") String email);
}
