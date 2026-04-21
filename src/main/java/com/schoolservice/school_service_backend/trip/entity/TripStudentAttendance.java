package com.schoolservice.school_service_backend.trip.entity;

import com.schoolservice.school_service_backend.student.entity.Student;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "trip_student_attendance",
        uniqueConstraints = @UniqueConstraint(name = "uk_trip_student", columnNames = {"trip_id", "student_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripStudentAttendance {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(nullable = false)
    @Builder.Default
    private boolean pickedUp = false;

    private LocalDateTime pickedUpAt;

    @Column(nullable = false)
    @Builder.Default
    private boolean droppedOff = false;

    private LocalDateTime droppedOffAt;
}
