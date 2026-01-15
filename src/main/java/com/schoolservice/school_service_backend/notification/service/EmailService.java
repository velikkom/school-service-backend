package com.schoolservice.school_service_backend.notification.service;

public interface EmailService {
    void sendPasswordResetEmail(String email, String token);
}
