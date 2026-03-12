package com.schoolservice.school_service_backend.student.entity;

import com.schoolservice.school_service_backend.parent.entity.Parent;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue
    private UUID id;

    /* =========================
       BASIC INFO
    ========================= */

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private LocalDate birthDate;

    private String gender;


    /* =========================
       SCHOOL INFO
    ========================= */

    private String schoolName;

    private String grade;

    private String className;


    /* =========================
       ADDRESS
    ========================= */

    @Column(nullable = false)
    private String address;

    private String district;

    private String city;


    /* =========================
       RELATION
    ========================= */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Parent parent;


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