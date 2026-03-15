package com.schoolservice.school_service_backend.hostess.service.ımpl;

import com.schoolservice.school_service_backend.hostess.dto.request.CreateHostessAssignmentRequest;
import com.schoolservice.school_service_backend.hostess.dto.request.UpdateHostessAssignmentRequest;
import com.schoolservice.school_service_backend.hostess.dto.response.HostessAssignmentResponse;
import com.schoolservice.school_service_backend.hostess.entity.Hostess;
import com.schoolservice.school_service_backend.hostess.entity.HostessAssignment;
import com.schoolservice.school_service_backend.hostess.mapper.HostessMapper;
import com.schoolservice.school_service_backend.hostess.repository.HostessAssignmentRepository;
import com.schoolservice.school_service_backend.hostess.repository.HostessRepository;
import com.schoolservice.school_service_backend.hostess.service.HostessAssignmentService;
import com.schoolservice.school_service_backend.route.entity.Route;
import com.schoolservice.school_service_backend.route.repository.RouteRepository;
import com.schoolservice.school_service_backend.vehicle.entity.Vehicle;
import com.schoolservice.school_service_backend.vehicle.repository.VehicleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HostessAssignmentServiceImpl implements HostessAssignmentService {

    private final HostessAssignmentRepository assignmentRepository;
    private final HostessRepository hostessRepository;
    private final RouteRepository routeRepository;
    private final VehicleRepository vehicleRepository;

    @Override
    public HostessAssignmentResponse createAssignment(UUID hostessId, CreateHostessAssignmentRequest request) {

        Hostess hostess = hostessRepository.findById(hostessId)
                .orElseThrow(() -> new EntityNotFoundException("Hostess not found"));

        Route route = routeRepository.findById(request.getRouteId())
                .orElseThrow(() -> new EntityNotFoundException("Route not found"));

        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new EntityNotFoundException("Vehicle not found"));

        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new IllegalArgumentException("Start time must be before end time");
        }

        validateNoConflict(
                hostessId,
                request.getDayOfWeek(),
                request.getStartTime(),
                request.getEndTime(),
                null
        );

        HostessAssignment assignment = HostessAssignment.builder()
                .hostess(hostess)
                .route(route)
                .vehicle(vehicle)
                .dayOfWeek(request.getDayOfWeek())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .notes(request.getNotes())
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        HostessAssignment saved = assignmentRepository.save(assignment);

        return HostessMapper.toHostessAssignmentResponse(saved);
    }

    @Override
    public List<HostessAssignmentResponse> getAssignmentsByHostessId(UUID hostessId) {

        return assignmentRepository.findByHostessId(hostessId)
                .stream()
                .map(HostessMapper::toHostessAssignmentResponse)
                .toList();
    }

    @Override
    public HostessAssignmentResponse getAssignmentById(UUID hostessId, UUID assignmentId) {

        HostessAssignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new EntityNotFoundException("Assignment not found"));

        if (!assignment.getHostess().getId().equals(hostessId)) {
            throw new IllegalArgumentException("Assignment does not belong to this hostess");
        }

        return HostessMapper.toHostessAssignmentResponse(assignment);
    }

    @Override
    public HostessAssignmentResponse updateAssignment(UUID hostessId,
                                                       UUID assignmentId,
                                                       UpdateHostessAssignmentRequest request) {

        HostessAssignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new EntityNotFoundException("Assignment not found"));

        if (!assignment.getHostess().getId().equals(hostessId)) {
            throw new IllegalArgumentException("Assignment does not belong to this hostess");
        }

        Route route = routeRepository.findById(request.getRouteId())
                .orElseThrow(() -> new EntityNotFoundException("Route not found"));

        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new EntityNotFoundException("Vehicle not found"));

        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new IllegalArgumentException("Start time must be before end time");
        }

        validateNoConflict(
                hostessId,
                request.getDayOfWeek(),
                request.getStartTime(),
                request.getEndTime(),
                assignmentId
        );

        assignment.setRoute(route);
        assignment.setVehicle(vehicle);
        assignment.setDayOfWeek(request.getDayOfWeek());
        assignment.setStartTime(request.getStartTime());
        assignment.setEndTime(request.getEndTime());
        assignment.setNotes(request.getNotes());
        assignment.setUpdatedAt(LocalDateTime.now());

        HostessAssignment updated = assignmentRepository.save(assignment);

        return HostessMapper.toHostessAssignmentResponse(updated);
    }

    @Override
    public void deleteAssignment(UUID hostessId, UUID assignmentId) {

        HostessAssignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new EntityNotFoundException("Assignment not found"));

        if (!assignment.getHostess().getId().equals(hostessId)) {
            throw new IllegalArgumentException("Assignment does not belong to this hostess");
        }

        assignmentRepository.delete(assignment);
    }

    @Override
    public List<HostessAssignmentResponse> getMyAssignments(UUID userId) {

        return assignmentRepository.findByHostessUserId(userId)
                .stream()
                .map(HostessMapper::toHostessAssignmentResponse)
                .toList();
    }

    private void validateNoConflict(UUID hostessId,
                                    java.time.DayOfWeek dayOfWeek,
                                    java.time.LocalTime startTime,
                                    java.time.LocalTime endTime,
                                    UUID excludeAssignmentId) {

        List<HostessAssignment> conflicts =
                assignmentRepository.findConflictingAssignments(
                        hostessId,
                        dayOfWeek,
                        startTime,
                        endTime,
                        excludeAssignmentId
                );

        if (!conflicts.isEmpty()) {
            throw new IllegalArgumentException("Hostess already has an assignment during this time");
        }
    }
}