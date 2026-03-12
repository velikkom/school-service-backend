package com.schoolservice.school_service_backend.route.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "routes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Route {

    @Id
    @GeneratedValue
    private UUID id;

    /* =========================
       BASIC INFO
    ========================= */

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String code;

    private String description;


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