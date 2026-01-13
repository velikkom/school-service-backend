package com.schoolservice.school_service_backend.user.controller;

import com.schoolservice.school_service_backend.common.response.ResponseWrapper;
import com.schoolservice.school_service_backend.user.dto.PendingUserResponse;
import com.schoolservice.school_service_backend.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(
        name = "Admin - User Management",
        description = "Admin operations for approving, rejecting and managing users"
)
public class AdminUserController {

    private final UserService userService;


    /**
     * Get all pending users
     * @return List of pending users
     */
    @GetMapping("/pending")
    @Operation(summary = "Get pending users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseWrapper<List<PendingUserResponse>>> getPendingUsers() {
        return ResponseEntity.ok(ResponseWrapper.success(
                userService.getPendingUsers(),
                "Pending users retrieved successfully"));
    }

    /**
     * Approve a user
     * @param userId The ID of the user to approve
     * @return Success message
     */
    @PutMapping("/{userId}/approve")
    @Operation(
            summary = "Approve user (ADMIN only)",
            description = "Approves a pending user. Only ADMIN role is allowed."
    )
    @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseWrapper<Void>> approveUser(@PathVariable UUID id) {
        userService.approveUser(id);
        return ResponseEntity.ok(ResponseWrapper.success(null, "User approved successfully"));
    }

    /**
     * Reject a user
     * @param userId The ID of the user to reject
     * @return Success message
     */
    @PutMapping("/{userId}/reject")
    @Operation(summary = "Reject user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseWrapper<Void>> rejectUser(UUID userId) {
        userService.rejectUser(userId);
        return ResponseEntity.ok(ResponseWrapper.success(null, "User rejected successfully"));
    }

}
