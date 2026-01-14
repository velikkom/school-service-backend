package com.schoolservice.school_service_backend.common.audit;

import java.util.UUID;

public interface AdminAuditService {

    void log(
            AuditAction action,
            UUID targetUserId,
            String details
    );
}
