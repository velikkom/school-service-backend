package com.schoolservice.school_service_backend.student.entity;

import com.schoolservice.school_service_backend.parent.entity.Parent;
import com.schoolservice.school_service_backend.route.entity.RouteStop;
import com.schoolservice.school_service_backend.student.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "students",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"parent_id", "firstName", "lastName", "birthDate"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue
    private UUID id;

    /* ================= BASIC INFO ================= */

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    /* ================= SCHOOL INFO ================= */

    private String schoolName;
    private String grade;
    private String className;

    /* ================= ADDRESS ================= */

    @Column(nullable = false)
    private String address;

    private String district;
    private String city;

    /* ================= RELATIONS ================= */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", nullable = false)
    private Parent parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_stop_id")
    private RouteStop routeStop;

    /* ================= STATUS ================= */

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;

    private LocalDateTime deletedAt;

    /* ================= AUDIT ================= */

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivate() {
        this.active = false;
        this.deletedAt = LocalDateTime.now();
    }
}