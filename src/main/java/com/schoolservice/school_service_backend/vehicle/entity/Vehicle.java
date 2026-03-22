package com.schoolservice.school_service_backend.vehicle.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;
@Entity
@Table(name = "vehicles",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_vehicle_plate", columnNames = "plateNumber")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, updatable = false)
    private String plateNumber;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private int capacity;

    @Column(nullable = false)
    private boolean active;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // =========================
    // 🔥 BUSINESS METHODS
    // =========================

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    public void updateDetails(String brand, String model, int capacity) {
        this.brand = brand;
        this.model = model;
        this.capacity = capacity;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.active = true;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}