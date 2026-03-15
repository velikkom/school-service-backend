package com.schoolservice.school_service_backend.hostess.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeHostessStatusRequest {

    @NotNull(message = "Active status is required")
    private Boolean active;
}