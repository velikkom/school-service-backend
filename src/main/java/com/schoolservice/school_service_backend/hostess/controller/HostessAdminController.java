package com.schoolservice.school_service_backend.hostess.controller;

import com.schoolservice.school_service_backend.common.response.PageResponse;
import com.schoolservice.school_service_backend.common.response.ResponseWrapper;
import com.schoolservice.school_service_backend.common.util.PageMapper;
import com.schoolservice.school_service_backend.hostess.dto.request.*;
import com.schoolservice.school_service_backend.hostess.dto.response.HostessAssignmentResponse;
import com.schoolservice.school_service_backend.hostess.dto.response.HostessResponse;
import com.schoolservice.school_service_backend.hostess.service.HostessAssignmentService;
import com.schoolservice.school_service_backend.hostess.service.HostessService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/hostesses")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class HostessAdminController {

    private final HostessService hostessService;
    private final HostessAssignmentService hostessAssignmentService;

    /**
     * Hostess crud
     * post mapping
     */

    @PostMapping
    @Operation(
            summary = "create hostess",
            description = "Create a new hostess (ADMIN only)"
    )
    public ResponseEntity<ResponseWrapper<HostessResponse>> createHostess(
            @Valid @RequestBody CreateHostessRequest request) {

        HostessResponse response = hostessService.createHostess(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ResponseWrapper.success(
                        response,
                        "Hostess created successfully"
                ));
    }

    /**
     * Get all hostesses (paginated)
     * @param pageable pageable
     * @return hostessResponse
     */


    @GetMapping
    @Operation(
            summary = "Get all hostesses (paginated)",
            description = "Returns paginated list of hostesses (ADMIN only)"
    )
    public ResponseEntity<ResponseWrapper<PageResponse<HostessResponse>>> getAllHostesses(
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable
    ) {

        var page = hostessService.getAllHostesses(pageable);

        var pageResponse = PageMapper.toPageResponse(page);

        return ResponseEntity.ok(
                ResponseWrapper.success(pageResponse)
        );
    }


    @GetMapping("/{hostessId}")
    @Operation(
            summary = "Get hostess by ID",
            description = "Returns hostess details by ID (ADMIN only)"
    )
    public ResponseEntity<ResponseWrapper<HostessResponse>> getHostessById(
            @PathVariable UUID hostessId
    ) {

        HostessResponse response = hostessService.getHostessById(hostessId);

        return ResponseEntity.ok(
                ResponseWrapper.success(response)
        );
    }


    @PutMapping("/{hostessId}")
    @Operation(
            summary = "Update hostess",
            description = "Updates hostess information (ADMIN only)"
    )
    public ResponseEntity<ResponseWrapper<HostessResponse>> updateHostess(
            @PathVariable UUID hostessId,
            @Valid @RequestBody UpdateHostessRequest request
    ) {

        HostessResponse response =
                hostessService.updateHostess(hostessId, request);

        return ResponseEntity.ok(
                ResponseWrapper.success(
                        response,
                        "Hostess updated successfully"
                )
        );
    }

    @PatchMapping("/{hostessId}/status")
    @Operation(
            summary = "Change hostess status",
            description = "Activate or deactivate hostess (ADMIN only)"
    )
    public ResponseEntity<ResponseWrapper<HostessResponse>> changeStatus(
            @PathVariable UUID hostessId,
            @Valid @RequestBody ChangeHostessStatusRequest request
    ) {

        HostessResponse response =
                hostessService.changeHostessStatus(hostessId, request);

        return ResponseEntity.ok(
                ResponseWrapper.success(
                        response,
                        "Hostess status updated"
                )
        );
    }

    @PostMapping("/{hostessId}/assignments")
    @Operation(
            summary = "Create hostess assignment",
            description = "Creates a new assignment for a hostess (ADMIN only)"
    )
    public ResponseEntity<ResponseWrapper<HostessAssignmentResponse>> createAssignment(
            @PathVariable UUID hostessId,
            @Valid @RequestBody CreateHostessAssignmentRequest request
    ) {

        HostessAssignmentResponse response =
                hostessAssignmentService.createAssignment(hostessId, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseWrapper.success(
                        response,
                        "Assignment created successfully"
                ));
    }

    @GetMapping("/{hostessId}/assignments/{assignmentId}")
    @Operation(
            summary = "Get assignment by ID",
            description = "Returns assignment details (ADMIN only)"
    )
    public ResponseEntity<ResponseWrapper<HostessAssignmentResponse>> getAssignmentById(
            @PathVariable UUID hostessId,
            @PathVariable UUID assignmentId
    ) {

        HostessAssignmentResponse response =
                hostessAssignmentService.getAssignmentById(hostessId, assignmentId);

        return ResponseEntity.ok(
                ResponseWrapper.success(response)
        );
    }

    @PutMapping("/{hostessId}/assignments/{assignmentId}")
    @Operation(
            summary = "Update assignment",
            description = "Updates assignment details (ADMIN only)"
    )
    public ResponseEntity<ResponseWrapper<HostessAssignmentResponse>> updateAssignment(
            @PathVariable UUID hostessId,
            @PathVariable UUID assignmentId,
            @Valid @RequestBody UpdateHostessAssignmentRequest request
    ) {

        HostessAssignmentResponse response =
                hostessAssignmentService.updateAssignment(hostessId, assignmentId, request);

        return ResponseEntity.ok(
                ResponseWrapper.success(
                        response,
                        "Assignment updated successfully"
                )
        );
    }

    @DeleteMapping("/{hostessId}/assignments/{assignmentId}")
    @Operation(
            summary = "Delete assignment",
            description = "Deletes assignment (ADMIN only)"
    )
    public ResponseEntity<ResponseWrapper<Void>> deleteAssignment(
            @PathVariable UUID hostessId,
            @PathVariable UUID assignmentId
    ) {

        hostessAssignmentService.deleteAssignment(hostessId, assignmentId);

        return ResponseEntity.ok(
                ResponseWrapper.success(
                        null,
                        "Assignment deleted successfully"
                )
        );
    }





}
