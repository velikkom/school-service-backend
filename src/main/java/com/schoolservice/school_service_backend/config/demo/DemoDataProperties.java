package com.schoolservice.school_service_backend.config.demo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Demo dataset toggled via {@code app.demo-data.enabled} (default {@code false} for production safety).
 */
@Component
@ConfigurationProperties(prefix = "app.demo-data")
@Getter
@Setter
public class DemoDataProperties {

    /**
     * When true, seeds demo users/entities once per database (idempotent).
     */
    private boolean enabled = false;

    /**
     * Password for all demo login accounts (parents, drivers, hostesses).
     */
    private String defaultPassword = "Demo123!";
}
