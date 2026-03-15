package com.schoolservice.school_service_backend.parent.repository;

import com.schoolservice.school_service_backend.parent.entity.Parent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ParentRepository extends JpaRepository<Parent, UUID> {
}
