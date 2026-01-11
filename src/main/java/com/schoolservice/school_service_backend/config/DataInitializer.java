package com.schoolservice.school_service_backend.config;

import com.schoolservice.school_service_backend.user.entity.User;
import com.schoolservice.school_service_backend.user.enums.RoleType;
import com.schoolservice.school_service_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        String adminEmail = "admin@school.com";

        if (userRepository.existsByEmail(adminEmail)) {
            return; // ✅ Zaten varsa dokunma
        }

        User admin = User.builder()
                .firstName("System")
                .lastName("Admin")
                .email(adminEmail)
                .password(passwordEncoder.encode("Admin123!"))
                .roles(Set.of(RoleType.ROLE_ADMIN))
                .active(true)
                .build();

        userRepository.save(admin);

        System.out.println("✅ Default ADMIN user created: " + adminEmail);
    }
}
