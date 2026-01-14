package com.schoolservice.school_service_backend.common.audit;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminAuditServiceImpl implements AdminAuditService {

    private final AdminAuditLogRepository repository;

    @Override
    public void log(
            AuditAction action,
            UUID targetUserId,
            String details
    ) {
        AdminAuditLog log = new AdminAuditLog();

        log.setAdminEmail(
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName()
        );
        log.setTargetUserId(targetUserId);
        log.setAction(action);
        log.setDetails(details);

        repository.save(log);
    }
}
