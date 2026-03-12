package com.schoolservice.school_service_backend.route.entity;

import com.schoolservice.school_service_backend.student.entity.Student;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "route_stops")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteStop {

    @Id
    @GeneratedValue
    private UUID id;

    /* =========================
       RELATIONS
    ========================= */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;


    /* =========================
       STOP INFO
    ========================= */

    @Column(nullable = false)
    private Integer stopOrder;

    @Column(nullable = false)
    private String addressText;

    private String note;


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