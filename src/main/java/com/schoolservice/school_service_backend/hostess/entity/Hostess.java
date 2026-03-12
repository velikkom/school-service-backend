package com.schoolservice.school_service_backend.hostess.entity;

import com.schoolservice.school_service_backend.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "hostesses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hostess {

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

    private String phone;

    private String emergencyContact;


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