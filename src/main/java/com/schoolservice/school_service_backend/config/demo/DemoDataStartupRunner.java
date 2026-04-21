package com.schoolservice.school_service_backend.config.demo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Runs after {@link com.schoolservice.school_service_backend.config.DataInitializer} (default order 0).
 */
@Component
@Order(100)
@ConditionalOnProperty(prefix = "app.demo-data", name = "enabled", havingValue = "true")
@RequiredArgsConstructor
@Slf4j
public class DemoDataStartupRunner implements CommandLineRunner {

    private final DemoDataSeedService demoDataSeedService;

    @Override
    public void run(String... args) {
        log.info("Demo data seeding is enabled; checking database...");
        demoDataSeedService.seedIfAbsent();
    }
}
