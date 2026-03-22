package com.schoolservice.school_service_backend.driver.entity;

import com.schoolservice.school_service_backend.user.entity.User;
import com.schoolservice.school_service_backend.vehicle.entity.Vehicle;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "drivers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Driver {


    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false, unique = true)
    private String licenseNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @Column(nullable = false)
    private boolean active;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // =========================
    // 🔥 BUSINESS METHODS
    // =========================

    public void assignVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public void removeVehicle() {
        this.vehicle = null;
    }

    public void deactivate() {
        this.active = false;
        this.vehicle = null;
    }

    public void activate() {
        this.active = true;
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

