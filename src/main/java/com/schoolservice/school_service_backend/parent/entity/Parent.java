package com.schoolservice.school_service_backend.parent.entity;

import com.schoolservice.school_service_backend.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "parents",
        indexes = {
                @Index(name = "idx_parent_user", columnList = "user_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Parent {

    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    // CONTACT
    @Column(nullable = true, length = 15)
    private String phoneNumber;

    private String emergencyContactName;
    private String emergencyContactPhone;

    // ADDRESS
    private String address;
    private String district;
    private String city;

    // 🔥 CRITICAL
    @Column(unique = true)
    private String identityNumber;

    // AUDIT
    @Column(nullable = true, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // 🔥 DERIVED LOGIC
    public boolean isProfileComplete() {
        return phoneNumber != null
                && address != null
                && city != null
                && district != null
                && identityNumber != null;
    }
    @Transient
    public ProfileStatus getProfileStatus() {
        return isProfileComplete() ? ProfileStatus.COMPLETE : ProfileStatus.INCOMPLETE;
    }

}