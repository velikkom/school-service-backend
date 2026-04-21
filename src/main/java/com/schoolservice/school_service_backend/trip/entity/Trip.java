package com.schoolservice.school_service_backend.trip.entity;

import com.schoolservice.school_service_backend.hostess.entity.Hostess;
import com.schoolservice.school_service_backend.hostess.entity.HostessAssignment;
import com.schoolservice.school_service_backend.route.entity.Route;
import com.schoolservice.school_service_backend.trip.enums.TripStatus;
import com.schoolservice.school_service_backend.vehicle.entity.Vehicle;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "trips", indexes = {
        @Index(name = "idx_trip_hostess_date_status", columnList = "hostess_id,service_date,status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trip {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "company_id")
    private UUID companyId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hostess_id", nullable = false)
    private Hostess hostess;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hostess_assignment_id")
    private HostessAssignment sourceAssignment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private TripStatus status = TripStatus.IN_PROGRESS;

    @Column(nullable = false)
    private LocalDate serviceDate;

    /**
     * Denormalized pointer to the route stop the hostess is expected to serve next / at.
     */
    @Column(name = "current_route_stop_id")
    private UUID currentRouteStopId;

    @Column(nullable = false)
    private LocalDateTime startedAt;

    private LocalDateTime completedAt;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<TripStopProgress> stopProgresses = new ArrayList<>();

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<TripStudentAttendance> studentAttendances = new ArrayList<>();

    public void addStopProgress(TripStopProgress progress) {
        stopProgresses.add(progress);
        progress.setTrip(this);
    }

    public void addStudentAttendance(TripStudentAttendance attendance) {
        studentAttendances.add(attendance);
        attendance.setTrip(this);
    }
}
