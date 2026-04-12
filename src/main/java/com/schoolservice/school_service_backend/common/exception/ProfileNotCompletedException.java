package com.schoolservice.school_service_backend.common.exception;

public class ProfileNotCompletedException extends BusinessException {

    public ProfileNotCompletedException() {
        super("PROFILE_NOT_COMPLETED");
    }
}