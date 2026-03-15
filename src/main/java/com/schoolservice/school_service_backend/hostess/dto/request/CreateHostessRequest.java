package com.schoolservice.school_service_backend.hostess.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateHostessRequest {

    @NotNull(message = "User id is required")
    private UUID userId;

    private String phoneNumber;

    private String emergencyContactName;

    private String emergencyContactPhone;
}