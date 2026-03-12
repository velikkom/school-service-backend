package com.schoolservice.school_service_backend.user.controller;

import com.schoolservice.school_service_backend.common.response.ResponseWrapper;
import com.schoolservice.school_service_backend.user.dto.*;
import com.schoolservice.school_service_backend.user.enums.RoleType;
import com.schoolservice.school_service_backend.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
     * Bulk approve users
     */
    @PutMapping(value = "/approve",
            consumes = "application/json")
    @Operation(
            summary = "Bulk approve users",
            description = "Approves multiple pending users and assigns the same role"
    )
    public ResponseEntity<ResponseWrapper<Void>> approveUsers(
            @Valid @RequestBody AdminBulkApproveRequest request
    ) {
        userService.approveUsers(request.userIds(), request.role());

        return ResponseEntity.ok(
                ResponseWrapper.success(
                        null,
                        "Users approved successfully"
                )
        );
    }


    /**
     * Approve a pending user
     */
    @PutMapping("/{userId}/approve")
    public ResponseEntity<ResponseWrapper<Void>> approveUser(
            @PathVariable UUID userId,
            @RequestParam RoleType role
    ) {
        userService.approveUser(userId, role);
        return ResponseEntity.ok(ResponseWrapper.success(null, "User approved"));
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

    /**
     * List approved users
     */
    @GetMapping("/approved")
    @Operation(
            summary = "Get approved users",
            description = "Lists all approved users (ADMIN only)"
    )
    public ResponseEntity<ResponseWrapper<List<AdminUserResponse>>> getApprovedUsers() {

        return ResponseEntity.ok(
                ResponseWrapper.success(
                        userService.getApprovedUsers(),
                        "Approved users retrieved successfully"
                )
        );
    }

    /**
     * Activate user
     */
    @PutMapping("/{userId}/activate")
    @Operation(
            summary = "Activate user",
            description = "Activates an approved but inactive user"
    )
    public ResponseEntity<ResponseWrapper<Void>> activateUser(
            @PathVariable UUID userId
    ) {
        userService.activateUser(userId);

        return ResponseEntity.ok(
                ResponseWrapper.success(
                        null,
                        "User activated successfully"
                )
        );
    }

    /**
     * Deactivate user
     */
    @PutMapping("/{userId}/deactivate")
    @Operation(
            summary = "Deactivate user",
            description = "Deactivates an active user"
    )
    public ResponseEntity<ResponseWrapper<Void>> deactivateUser(
            @PathVariable UUID userId
    ) {
        userService.deactivateUser(userId);

        return ResponseEntity.ok(
                ResponseWrapper.success(
                        null,
                        "User deactivated successfully"
                )
        );
    }

    /**
     * Get user detail by ID
     */
    @GetMapping("/{userId}")
    @Operation(
            summary = "Get  user detail",
            description = "Returns user detail by ID (ADMIN only)"
    )
    public ResponseEntity<ResponseWrapper<AdminUserResponse>> getUserById(@PathVariable UUID userId) {
        return ResponseEntity.ok(
                ResponseWrapper.success(
                        userService.getUserDetailForAdmin(userId),
                        "User retrieved successfully"
                )
        );
    }


    /**
     * Get users with filters and sorting
     */
    @GetMapping
    @Operation(
            summary = "Get users",
            description = "Returns users with filters and sorting (ADMIN only)"
    )
    public ResponseEntity<ResponseWrapper<Page<AdminUserResponse>>> getAllUsers(
            AdminUserFilterRequest filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,   // 🔥 FIX
            @RequestParam(defaultValue = "desc") String sortDirection
    ) {
        return ResponseEntity.ok(
                ResponseWrapper.success(
                        userService.getAllUsersForAdmin(
                                filter,
                                page,
                                size,
                                sortBy,
                                sortDirection
                        ),
                        "Users retrieved successfully"
                )
        );
    }


    /**
     * Restore user
     */
    @PutMapping("/{userId}/restore")
    @Operation(
            summary = "Restore user",
            description = "Restores a soft deleted user (ADMIN only)"
    )
    public ResponseEntity<ResponseWrapper<Void>> restoreUser(
            @PathVariable UUID userId
    ) {
        userService.restoreUser(userId);

        return ResponseEntity.ok(
                ResponseWrapper.success(
                        null,
                        "User restored successfully"
                )
        );
    }


    /**
     * Register user
     */
    @PostMapping("/register")
    @Operation(
            summary = "Register user",
            description = "Registers a new user (ADMIN only)"
    )
    public ResponseEntity<ResponseWrapper<Void>> register(
            @Valid @RequestBody CreateUserRequest request
    ) {

        userService.registerUser(request);

        return ResponseEntity.ok(
                ResponseWrapper.success(
                        null,
                        "Registration successful. Waiting for admin approval."
                )
        );
    }

    /**
     * Update user
     */

    @PutMapping("/{userId}")
    @Operation(
            summary = "Update user",
            description = "Updates a user (ADMIN only)"
    )
    public ResponseWrapper<AdminUserResponse> updateUser(
            @PathVariable UUID userId,
            @Valid @RequestBody UpdateUserRequest request
    ) {
        AdminUserResponse updateUser = userService.updateUser(userId, request);
        return ResponseWrapper.success(updateUser,"User updated successfully");

    }



}