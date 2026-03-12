package com.schoolservice.school_service_backend.parent.entity;

import com.schoolservice.school_service_backend.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "parents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Parent {

    @Id
    @GeneratedValue
    private UUID id;

    /* =========================
       USER RELATION
    ========================= */

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    /* =========================
       CONTACT INFO
    ========================= */

    @Column(nullable = false)
    private String phoneNumber;

    private String emergencyContactName;

    private String emergencyContactPhone;

    /* =========================
       ADDRESS
    ========================= */

    private String address;

    private String district;

    private String city;

    /* =========================
       STATUS
    ========================= */

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;

    /* =========================
       AUDIT
    ========================= */

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;
}