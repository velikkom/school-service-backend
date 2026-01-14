package com.schoolservice.school_service_backend.user.specification;

import com.schoolservice.school_service_backend.user.dto.AdminUserFilterRequest;
import com.schoolservice.school_service_backend.user.entity.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserSpecification {

    public static Specification<User> withFilters(AdminUserFilterRequest f) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            /* =============================
               SOFT DELETE (DEFAULT)
            ============================== */
            if (!Boolean.TRUE.equals(f.includeDeleted())) {
                predicates.add(
                        cb.isNull(root.get("deletedAt"))
                );
            }

            /* =============================
               APPROVAL STATUS
            ============================== */
            if (f.approvalStatus() != null) {
                predicates.add(
                        cb.equal(root.get("approvalStatus"), f.approvalStatus())
                );
            }

            /* =============================
               ACTIVE / INACTIVE
            ============================== */
            if (f.active() != null) {
                predicates.add(
                        cb.equal(root.get("active"), f.active())
                );
            }

            /* =============================
               ROLE
            ============================== */
            if (f.role() != null) {
                predicates.add(
                        cb.isMember(f.role(), root.get("roles"))
                );
            }

            /* =============================
               EMAIL (LIKE, case-insensitive)
            ============================== */
            if (f.email() != null && !f.email().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("email")),
                                "%" + f.email().toLowerCase() + "%"
                        )
                );
            }

            /* =============================
               FIRST NAME
            ============================== */
            if (f.firstName() != null && !f.firstName().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("firstName")),
                                "%" + f.firstName().toLowerCase() + "%"
                        )
                );
            }

            /* =============================
               LAST NAME
            ============================== */
            if (f.lastName() != null && !f.lastName().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("lastName")),
                                "%" + f.lastName().toLowerCase() + "%"
                        )
                );
            }

            /* =============================
               CREATED AT - FROM
            ============================== */
            if (f.createdAtFrom() != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("createdAt"),
                                f.createdAtFrom().atStartOfDay()
                        )
                );
            }

            /* =============================
               CREATED AT - TO
            ============================== */
            if (f.createdAtTo() != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.get("createdAt"),
                                f.createdAtTo().atTime(23, 59, 59)
                        )
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

