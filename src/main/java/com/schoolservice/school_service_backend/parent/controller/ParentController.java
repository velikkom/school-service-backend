package com.schoolservice.school_service_backend.parent.controller;

import com.schoolservice.school_service_backend.parent.dto.request.UpdateParentRequest;
import com.schoolservice.school_service_backend.parent.dto.response.ParentResponse;
import com.schoolservice.school_service_backend.parent.service.ParentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/parent")
@RequiredArgsConstructor
public class ParentController {

    private final ParentService parentService;

    /**
     * 🔥 Parent kendi profilini getirir
     */
    @GetMapping("/me")
    @Operation(summary = "Parent kendi profilini getirir")
    public ParentResponse getMyProfile(
            @AuthenticationPrincipal UUID userId
    ) {
        return parentService.getMyProfile(userId);
    }

    /**
     * 🔥 Parent profil günceller
     */
    @PutMapping("/me")
    @Operation(summary = "Parent profil günceller")
    public ParentResponse updateProfile(
            @AuthenticationPrincipal UUID userId,
            @RequestBody UpdateParentRequest request
    ) {
        return parentService.updateProfile(userId, request);
    }
}