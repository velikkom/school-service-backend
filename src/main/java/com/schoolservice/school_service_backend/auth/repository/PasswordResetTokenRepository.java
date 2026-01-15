package com.schoolservice.school_service_backend.auth.repository;

import com.schoolservice.school_service_backend.auth.entity.PasswordResetToken;
import com.schoolservice.school_service_backend.user.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface PasswordResetTokenRepository
        extends JpaRepository<PasswordResetToken, UUID> {

    Optional<PasswordResetToken> findByToken(String token);


    @Modifying
    @Transactional
    @Query("delete from PasswordResetToken prt where prt.user.id = :userId")
    void deleteByUserId(@Param("userId") UUID userId);
}
