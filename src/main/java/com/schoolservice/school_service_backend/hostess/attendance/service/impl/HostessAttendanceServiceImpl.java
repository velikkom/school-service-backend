package com.schoolservice.school_service_backend.hostess.attendance.service.impl;

import com.schoolservice.school_service_backend.common.exception.BusinessException;
import com.schoolservice.school_service_backend.common.exception.ResourceNotFoundException;
import com.schoolservice.school_service_backend.hostess.attendance.dto.response.ActiveAttendanceResponse;
import com.schoolservice.school_service_backend.hostess.attendance.dto.response.StopAttendanceRowResponse;
import com.schoolservice.school_service_backend.hostess.attendance.dto.response.StudentAttendanceRowResponse;
import com.schoolservice.school_service_backend.hostess.attendance.dto.response.TripSummaryResponse;
import com.schoolservice.school_service_backend.hostess.attendance.service.HostessAttendanceService;
import com.schoolservice.school_service_backend.hostess.entity.Hostess;
import com.schoolservice.school_service_backend.hostess.entity.HostessAssignment;
import com.schoolservice.school_service_backend.hostess.repository.HostessAssignmentRepository;
import com.schoolservice.school_service_backend.hostess.repository.HostessRepository;
import com.schoolservice.school_service_backend.route.entity.RouteStop;
import com.schoolservice.school_service_backend.route.repository.RouteStopRepository;
import com.schoolservice.school_service_backend.student.entity.Student;
import com.schoolservice.school_service_backend.trip.entity.Trip;
import com.schoolservice.school_service_backend.trip.entity.TripStopProgress;
import com.schoolservice.school_service_backend.trip.entity.TripStudentAttendance;
import com.schoolservice.school_service_backend.trip.enums.TripStatus;
import com.schoolservice.school_service_backend.trip.repository.TripRepository;
import com.schoolservice.school_service_backend.trip.repository.TripStopProgressRepository;
import com.schoolservice.school_service_backend.trip.repository.TripStudentAttendanceRepository;
import com.schoolservice.school_service_backend.user.entity.User;
import com.schoolservice.school_service_backend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class HostessAttendanceServiceImpl implements HostessAttendanceService {

    private final HostessRepository hostessRepository;
    private final UserRepository userRepository;
    private final HostessAssignmentRepository hostessAssignmentRepository;
    private final TripRepository tripRepository;
    private final TripStopProgressRepository tripStopProgressRepository;
    private final TripStudentAttendanceRepository tripStudentAttendanceRepository;
    private final RouteStopRepository routeStopRepository;

    @Override
    public ActiveAttendanceResponse getActiveScreen(String authenticatedUserEmail) {
        Trip trip = resolveOrStartTrip(authenticatedUserEmail);
        return mapToResponse(trip);
    }

    @Override
    public ActiveAttendanceResponse markStopArrived(String authenticatedUserEmail, UUID routeStopId) {
        Trip trip = loadTripForMutation(authenticatedUserEmail);
        List<TripStopProgress> ordered = tripStopProgressRepository.findAllForTripOrdered(trip.getId());
        TripStopProgress target = findProgressOrThrow(ordered, routeStopId);
        assertStopBelongsToTripRoute(trip, target.getRouteStop().getId());

        UUID expectedNext = computeNextArrivableStopId(ordered);
        if (expectedNext == null) {
            throw new BusinessException("All stops are completed");
        }
        if (!expectedNext.equals(routeStopId)) {
            throw new BusinessException("Stops must be visited in order");
        }

        if (target.getArrivedAt() == null) {
            target.setArrivedAt(LocalDateTime.now());
        }
        trip.setCurrentRouteStopId(routeStopId);
        tripRepository.save(trip);
        return mapToResponse(trip);
    }

    @Override
    public ActiveAttendanceResponse markStopDeparted(String authenticatedUserEmail, UUID routeStopId) {
        Trip trip = loadTripForMutation(authenticatedUserEmail);
        List<TripStopProgress> ordered = tripStopProgressRepository.findAllForTripOrdered(trip.getId());
        TripStopProgress target = findProgressOrThrow(ordered, routeStopId);

        if (target.getArrivedAt() == null) {
            throw new BusinessException("Cannot depart before arriving at the stop");
        }
        if (target.getDepartedAt() != null) {
            throw new BusinessException("Stop already departed");
        }

        target.setDepartedAt(LocalDateTime.now());

        List<TripStopProgress> refreshed = tripStopProgressRepository.findAllForTripOrdered(trip.getId());
        trip.setCurrentRouteStopId(resolveCurrentStopId(refreshed));
        tripRepository.save(trip);
        return mapToResponse(trip);
    }

    @Override
    public ActiveAttendanceResponse markStudentPickup(String authenticatedUserEmail, UUID studentId) {
        Trip trip = loadTripForMutation(authenticatedUserEmail);
        TripStudentAttendance row = tripStudentAttendanceRepository
                .findByTrip_IdAndStudent_Id(trip.getId(), studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not on this trip"));

        List<TripStopProgress> ordered = tripStopProgressRepository.findAllForTripOrdered(trip.getId());
        UUID currentStop = resolveCurrentStopId(ordered);
        UUID studentStopId = resolveStudentRouteStopId(trip.getRoute().getId(), studentId);
        if (!Objects.equals(currentStop, studentStopId)) {
            throw new BusinessException("Pickup is only allowed at the student's stop when it is the active stop");
        }

        if (row.isPickedUp()) {
            throw new BusinessException("Student already picked up");
        }

        row.setPickedUp(true);
        row.setPickedUpAt(LocalDateTime.now());
        return mapToResponse(trip);
    }

    @Override
    public ActiveAttendanceResponse markStudentDropoff(String authenticatedUserEmail, UUID studentId) {
        Trip trip = loadTripForMutation(authenticatedUserEmail);
        TripStudentAttendance row = tripStudentAttendanceRepository
                .findByTrip_IdAndStudent_Id(trip.getId(), studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not on this trip"));

        if (!row.isPickedUp()) {
            throw new BusinessException("Student must be picked up before drop-off");
        }
        if (row.isDroppedOff()) {
            throw new BusinessException("Student already dropped off");
        }

        row.setDroppedOff(true);
        row.setDroppedOffAt(LocalDateTime.now());
        return mapToResponse(trip);
    }

    private Trip resolveOrStartTrip(String email) {
        Hostess hostess = requireHostess(email);
        User user = requireUser(email);
        UUID companyId = user.getCompanyId();
        LocalDate today = LocalDate.now();

        Optional<Trip> existing = tripRepository.findActiveTripForHostess(
                hostess.getId(),
                today,
                TripStatus.IN_PROGRESS,
                companyId
        );
        if (existing.isPresent()) {
            return existing.get();
        }
        return startTripFromAssignment(hostess, user, companyId, today)
                .orElseThrow(() -> new ResourceNotFoundException("No active trip"));
    }

    private Trip loadTripForMutation(String email) {
        Hostess hostess = requireHostess(email);
        User user = requireUser(email);
        UUID companyId = user.getCompanyId();
        LocalDate today = LocalDate.now();
        return tripRepository
                .findActiveTripForHostess(hostess.getId(), today, TripStatus.IN_PROGRESS, companyId)
                .orElseThrow(() -> new ResourceNotFoundException("No active trip"));
    }

    private Optional<Trip> startTripFromAssignment(Hostess hostess, User user, UUID companyId, LocalDate today) {
        List<HostessAssignment> assignments = hostessAssignmentRepository
                .findActiveAssignmentsForHostessAndDay(hostess.getId(), today.getDayOfWeek());

        Optional<HostessAssignment> chosen = assignments.stream()
                .filter(ha -> tenantMatches(ha.getRoute().getCompanyId(), companyId))
                .filter(ha -> ha.getRoute().isActive())
                .filter(ha -> ha.getVehicle().isActive())
                .min(Comparator.comparing(HostessAssignment::getStartTime));

        if (chosen.isEmpty()) {
            return Optional.empty();
        }

        HostessAssignment assignment = chosen.get();
        List<RouteStop> routeStops = routeStopRepository.findWithRouteAndStudentByRouteId(assignment.getRoute().getId());
        if (routeStops.isEmpty()) {
            return Optional.empty();
        }

        UUID firstStopId = routeStops.get(0).getId();

        Trip trip = Trip.builder()
                .companyId(assignment.getRoute().getCompanyId())
                .hostess(hostess)
                .route(assignment.getRoute())
                .vehicle(assignment.getVehicle())
                .sourceAssignment(assignment)
                .status(TripStatus.IN_PROGRESS)
                .serviceDate(today)
                .currentRouteStopId(firstStopId)
                .startedAt(LocalDateTime.now())
                .build();

        for (RouteStop rs : routeStops) {
            trip.addStopProgress(TripStopProgress.builder()
                    .routeStop(rs)
                    .build());
        }

        List<Student> studentsOnRoute = routeStops.stream()
                .map(RouteStop::getStudent)
                .distinct()
                .toList();

        for (Student student : studentsOnRoute) {
            trip.addStudentAttendance(TripStudentAttendance.builder()
                    .student(student)
                    .pickedUp(false)
                    .droppedOff(false)
                    .build());
        }

        return Optional.of(tripRepository.save(trip));
    }

    private ActiveAttendanceResponse mapToResponse(Trip trip) {
        UUID tripId = trip.getId();
        List<TripStopProgress> ordered = tripStopProgressRepository.findAllForTripOrdered(tripId);
        List<TripStudentAttendance> studentRows = tripStudentAttendanceRepository.findForTrip(tripId);

        List<RouteStop> routeStops = routeStopRepository.findWithRouteAndStudentByRouteId(trip.getRoute().getId());
        Map<UUID, RouteStop> stopByStudentId = routeStops.stream()
                .collect(Collectors.toMap(rs -> rs.getStudent().getId(), Function.identity(), (a, b) -> a));

        UUID currentStopId = resolveCurrentStopId(ordered);

        StopAttendanceRowResponse current = ordered.stream()
                .filter(p -> p.getRouteStop().getId().equals(currentStopId))
                .findFirst()
                .map(this::toStopRow)
                .orElse(null);

        List<StopAttendanceRowResponse> stops = ordered.stream()
                .map(this::toStopRow)
                .toList();

        List<StudentAttendanceRowResponse> students = studentRows.stream()
                .map(tsa -> toStudentRow(tsa, stopByStudentId))
                .toList();

        TripSummaryResponse summary = TripSummaryResponse.builder()
                .tripId(trip.getId())
                .status(trip.getStatus())
                .serviceDate(trip.getServiceDate())
                .routeId(trip.getRoute().getId())
                .routeName(trip.getRoute().getName())
                .routeCode(trip.getRoute().getCode())
                .vehicleId(trip.getVehicle().getId())
                .vehiclePlate(trip.getVehicle().getPlateNumber())
                .currentRouteStopId(currentStopId)
                .build();

        return ActiveAttendanceResponse.builder()
                .trip(summary)
                .currentStop(current)
                .stops(stops)
                .students(students)
                .build();
    }

    private StopAttendanceRowResponse toStopRow(TripStopProgress p) {
        RouteStop rs = p.getRouteStop();
        Student s = rs.getStudent();
        return StopAttendanceRowResponse.builder()
                .routeStopId(rs.getId())
                .stopOrder(rs.getStopOrder())
                .addressText(rs.getAddressText())
                .studentId(s.getId())
                .studentFirstName(s.getFirstName())
                .studentLastName(s.getLastName())
                .arrivedAt(p.getArrivedAt())
                .departedAt(p.getDepartedAt())
                .build();
    }

    private StudentAttendanceRowResponse toStudentRow(
            TripStudentAttendance tsa,
            Map<UUID, RouteStop> stopByStudentId
    ) {
        Student s = tsa.getStudent();
        RouteStop rs = stopByStudentId.get(s.getId());
        return StudentAttendanceRowResponse.builder()
                .studentId(s.getId())
                .firstName(s.getFirstName())
                .lastName(s.getLastName())
                .routeStopId(rs != null ? rs.getId() : null)
                .stopOrder(rs != null ? rs.getStopOrder() : null)
                .pickedUp(tsa.isPickedUp())
                .pickedUpAt(tsa.getPickedUpAt())
                .droppedOff(tsa.isDroppedOff())
                .droppedOffAt(tsa.getDroppedOffAt())
                .build();
    }

    /**
     * First stop (by order) that has not been departed yet; null if all completed.
     */
    private UUID resolveCurrentStopId(List<TripStopProgress> ordered) {
        return ordered.stream()
                .filter(p -> p.getDepartedAt() == null)
                .map(p -> p.getRouteStop().getId())
                .findFirst()
                .orElse(null);
    }

    /**
     * Next stop that may receive an arrive event: first in order without departure;
     * if that stop already has arrive, still the same id until departed.
     */
    private UUID computeNextArrivableStopId(List<TripStopProgress> ordered) {
        for (TripStopProgress p : ordered) {
            if (p.getDepartedAt() != null) {
                continue;
            }
            return p.getRouteStop().getId();
        }
        return null;
    }

    private TripStopProgress findProgressOrThrow(List<TripStopProgress> ordered, UUID routeStopId) {
        return ordered.stream()
                .filter(p -> p.getRouteStop().getId().equals(routeStopId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Stop not found on this trip"));
    }

    private void assertStopBelongsToTripRoute(Trip trip, UUID routeStopId) {
        boolean ok = routeStopRepository.findByIdAndRoute_Id(routeStopId, trip.getRoute().getId()).isPresent();
        if (!ok) {
            throw new ResourceNotFoundException("Stop not found on this trip");
        }
    }

    private UUID resolveStudentRouteStopId(UUID routeId, UUID studentId) {
        return routeStopRepository.findWithRouteAndStudentByRouteId(routeId).stream()
                .filter(rs -> rs.getStudent().getId().equals(studentId))
                .map(RouteStop::getId)
                .findFirst()
                .orElse(null);
    }

    private boolean tenantMatches(UUID routeCompanyId, UUID userCompanyId) {
        return Objects.equals(routeCompanyId, userCompanyId);
    }

    private Hostess requireHostess(String email) {
        return hostessRepository.findByUser_Email(email)
                .orElseThrow(() -> new ResourceNotFoundException("Hostess profile not found"));
    }

    private User requireUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
