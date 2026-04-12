package com.schoolservice.school_service_backend.parent.controller;

import com.schoolservice.school_service_backend.common.response.ResponseWrapper;
import com.schoolservice.school_service_backend.parent.dto.request.UpdateParentRequest;
import com.schoolservice.school_service_backend.parent.dto.response.ParentResponse;
import com.schoolservice.school_service_backend.parent.service.ParentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/parent")
@RequiredArgsConstructor
public class ParentController {

    private final ParentService parentService;

    @GetMapping("/me")
    @Operation(summary = "Get current parent profile")
    public ResponseEntity<ResponseWrapper<ParentResponse>> getMyProfile(
            Authentication authentication
    ) {
        String email = (String) authentication.getPrincipal();
        ParentResponse response = parentService.getMyProfileByEmail(email);
        return ResponseEntity.ok(ResponseWrapper.success(response, "Parent profile fetched"));
    }

    @PutMapping("/me")
    @Operation(summary = "Update parent profile")
    public ResponseEntity<ResponseWrapper<ParentResponse>> updateProfile(
            Authentication authentication,
            @RequestBody @Valid UpdateParentRequest request
    ) {
        String email = (String) authentication.getPrincipal();
        ParentResponse parentResponse = parentService.updateProfileByEmail(email, request);
        return ResponseEntity.ok(
                ResponseWrapper.success(parentResponse, "Parent profile updated")
        );
    }
}
