package com.schoolservice.school_service_backend.config.demo;

/**
 * Loads deterministic demo data for local / integration testing.
 */
public interface DemoDataSeedService {

    /**
     * Inserts demo rows if {@link DemoDataProperties#isEnabled()} and data is not already present.
     */
    void seedIfAbsent();
}
