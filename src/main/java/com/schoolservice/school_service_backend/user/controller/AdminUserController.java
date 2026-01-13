package com.schoolservice.school_service_backend.user.controller;

import com.schoolservice.school_service_backend.common.response.ResponseWrapper;
import com.schoolservice.school_service_backend.user.dto.PendingUserResponse;
import com.schoolservice.school_service_backend.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(
        name = "Admin - User Management",
        description = "Admin operations for approving, rejecting and managing users"
)
public class AdminUserController {

    private final UserService userService;

    /**
     * Get all users with PENDING approval status
     */
    @GetMapping("/pending")
    @Operation(
            summary = "Get pending users",
            description = "Returns all users waiting for admin approval"
    )
    public ResponseEntity<ResponseWrapper<List<PendingUserResponse>>> getPendingUsers() {

        return ResponseEntity.ok(
                ResponseWrapper.success(
                        userService.getPendingUsers(),
                        "Pending users retrieved successfully"
                )
        );
    }

    /**
     * Approve a pending user
     */
    @PutMapping("/{userId}/approve")
    @Operation(
            summary = "Approve user",
            description = "Approves a pending user. Only ADMIN can perform this operation."
    )
    public ResponseEntity<ResponseWrapper<Void>> approveUser(
            @PathVariable UUID userId
    ) {
        userService.approveUser(userId);

        return ResponseEntity.ok(
                ResponseWrapper.success(
                        null,
                        "User approved successfully"
                )
        );
    }

    /**
     * Reject a pending user
     */
    @PutMapping("/{userId}/reject")
    @Operation(
            summary = "Reject user",
            description = "Rejects a pending user. Only ADMIN can perform this operation."
    )
    public ResponseEntity<ResponseWrapper<Void>> rejectUser(
            @PathVariable UUID userId
    ) {
        userService.rejectUser(userId);

        return ResponseEntity.ok(
                ResponseWrapper.success(
                        null,
                        "User rejected successfully"
                )
        );
    }
}
