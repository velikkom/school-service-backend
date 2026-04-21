package com.schoolservice.school_service_backend.trip.entity;

import com.schoolservice.school_service_backend.route.entity.RouteStop;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "trip_stop_progress",
        uniqueConstraints = @UniqueConstraint(name = "uk_trip_stop", columnNames = {"trip_id", "route_stop_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripStopProgress {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "route_stop_id", nullable = false)
    private RouteStop routeStop;

    private LocalDateTime arrivedAt;

    private LocalDateTime departedAt;
}
