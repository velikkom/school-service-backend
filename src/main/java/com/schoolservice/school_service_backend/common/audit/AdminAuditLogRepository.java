package com.schoolservice.school_service_backend.common.audit;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AdminAuditLogRepository
        extends JpaRepository<AdminAuditLog, UUID> {
}
