package com.schoolservice.school_service_backend.config.demo;

/**
 * Stable identifiers for idempotent demo seeding.
 */
public final class DemoDataConstants {

    public static final String PARENT_EMAIL_PREFIX = "demo-parent-";
    public static final String PARENT_EMAIL_DOMAIN = "@demo.school.local";

    public static final String DRIVER_EMAIL_PREFIX = "demo-driver-";
    public static final String DRIVER_EMAIL_DOMAIN = "@demo.school.local";

    public static final String HOSTESS_EMAIL_PREFIX = "demo-hostess-";
    public static final String HOSTESS_EMAIL_DOMAIN = "@demo.school.local";

    public static final int PARENT_STUDENT_COUNT = 40;
    public static final int FLEET_COUNT = 4;

    public static String parentEmail(int index) {
        return PARENT_EMAIL_PREFIX + index + PARENT_EMAIL_DOMAIN;
    }

    public static String driverEmail(int index) {
        return DRIVER_EMAIL_PREFIX + index + DRIVER_EMAIL_DOMAIN;
    }

    public static String hostessEmail(int index) {
        return HOSTESS_EMAIL_PREFIX + index + HOSTESS_EMAIL_DOMAIN;
    }

    private DemoDataConstants() {
    }
}
