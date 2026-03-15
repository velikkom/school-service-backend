package com.schoolservice.school_service_backend.hostess.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateHostessRequest {

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    private String emergencyContactName;

    private String emergencyContactPhone;
}