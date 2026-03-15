package com.schoolservice.school_service_backend.hostess.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HostessResponse {

    private UUID id;

    private UUID userId;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String emergencyContactName;

    private String emergencyContactPhone;

    private boolean active;

    private LocalDateTime createdAt;
}