package com.schoolservice.school_service_backend.vehicle.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {

    @Id
    @GeneratedValue
    private UUID id;

    /* =========================
       BASIC INFO
    ========================= */

    @Column(nullable = false, unique = true)
    private String plateNumber;

    private String brand;

    private String model;

    private Integer capacity;

    private String color;


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