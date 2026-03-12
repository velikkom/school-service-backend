package com.schoolservice.school_service_backend.hostess.entity;

import com.schoolservice.school_service_backend.route.entity.Route;
import com.schoolservice.school_service_backend.vehicle.entity.Vehicle;
import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "hostess_assignments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HostessAssignment {

    @Id
    @GeneratedValue
    private UUID id;


    /* =========================
       RELATIONS
    ========================= */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hostess_id", nullable = false)
    private Hostess hostess;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;


    /* =========================
       SCHEDULE
    ========================= */

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;


    /* =========================
       STATUS
    ========================= */

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;


    /* =========================
       EXTRA
    ========================= */

    private String notes;


    /* =========================
       AUDIT
    ========================= */

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

}