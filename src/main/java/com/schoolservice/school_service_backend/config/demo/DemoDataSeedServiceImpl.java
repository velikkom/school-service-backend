package com.schoolservice.school_service_backend.config.demo;

import com.schoolservice.school_service_backend.driver.entity.Driver;
import com.schoolservice.school_service_backend.driver.repository.DriverRepository;
import com.schoolservice.school_service_backend.hostess.entity.Hostess;
import com.schoolservice.school_service_backend.hostess.repository.HostessRepository;
import com.schoolservice.school_service_backend.parent.entity.Parent;
import com.schoolservice.school_service_backend.parent.repository.ParentRepository;
import com.schoolservice.school_service_backend.student.entity.Student;
import com.schoolservice.school_service_backend.student.enums.Gender;
import com.schoolservice.school_service_backend.student.repository.StudentRepository;
import com.schoolservice.school_service_backend.user.entity.User;
import com.schoolservice.school_service_backend.user.enums.ApprovalStatus;
import com.schoolservice.school_service_backend.user.enums.RoleType;
import com.schoolservice.school_service_backend.user.repository.UserRepository;
import com.schoolservice.school_service_backend.vehicle.entity.Vehicle;
import com.schoolservice.school_service_backend.vehicle.repository.VehicleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class DemoDataSeedServiceImpl implements DemoDataSeedService {

    private static final String[] PARENT_FIRST_NAMES = {
            "Ayşe", "Mehmet", "Zeynep", "Ali", "Elif", "Mustafa", "Fatma", "Emre", "Selin", "Burak",
            "Deniz", "Can", "Ece", "Kerem", "Seda", "Onur", "Gizem", "Tolga", "Pınar", "Serkan",
            "Derya", "Volkan", "Aslı", "Murat", "Burcu", "Cem", "Hande", "Barış", "Melis", "Kaan",
            "Özge", "Yasin", "Tuğçe", "Hakan", "İrem", "Oğuz", "Cansu", "Erkan", "Şule", "Berk"
    };

    private static final String[] STUDENT_FIRST_NAMES = {
            "Defne", "Arda", "Yağmur", "Eren", "Naz", "Alp", "İpek", "Kuzey", "Lara", "Mert",
            "Nil", "Rüzgar", "Sude", "Toprak", "Ümit", "Vera", "Yaren", "Zeki", "Ada", "Bora",
            "Ceylin", "Doruk", "Eylül", "Fırat", "Gökçe", "Halil", "İdil", "Jale", "Koray", "Lina",
            "Miray", "Nejat", "Oya", "Polat", "Rana", "Sinan", "Tuba", "Umut", "Vildan", "Yiğit"
    };

    private static final String[] HOSTESS_FIRST = {"Esra", "Gül", "Nihan", "Sevim"};
    private static final String[] HOSTESS_LAST = {"Koç", "Demir", "Aydın", "Çelik"};
    private static final String[] DRIVER_FIRST = {"Ahmet", "Hasan", "İbrahim", "Osman"};
    private static final String[] DRIVER_LAST = {"Yıldız", "Kaya", "Şahin", "Öztürk"};

    private static final String[] VEHICLE_BRANDS = {"Mercedes-Benz", "Ford", "Iveco", "Fiat"};
    private static final String[] VEHICLE_MODELS = {"Sprinter", "Transit", "Daily", "Ducato"};

    private final DemoDataProperties demoDataProperties;
    private final UserRepository userRepository;
    private final ParentRepository parentRepository;
    private final StudentRepository studentRepository;
    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;
    private final HostessRepository hostessRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void seedIfAbsent() {
        if (!demoDataProperties.isEnabled()) {
            return;
        }
        if (userRepository.existsByEmail(DemoDataConstants.parentEmail(0))) {
            log.info("Demo data already present (marker user {}). Skipping.", DemoDataConstants.parentEmail(0));
            return;
        }

        String rawPassword = demoDataProperties.getDefaultPassword();
        String encoded = passwordEncoder.encode(rawPassword);

        List<Vehicle> vehicles = seedVehicles();
        seedDrivers(encoded, vehicles);
        seedHostesses(encoded);
        seedParentsAndStudents(encoded);

        log.info(
                "Demo data seeded: {} parents/students, {} vehicles, {} drivers, {} hostesses. Demo password: {}",
                DemoDataConstants.PARENT_STUDENT_COUNT,
                DemoDataConstants.FLEET_COUNT,
                DemoDataConstants.FLEET_COUNT,
                DemoDataConstants.FLEET_COUNT,
                rawPassword
        );
    }

    private List<Vehicle> seedVehicles() {
        List<Vehicle> saved = new java.util.ArrayList<>();
        for (int i = 0; i < DemoDataConstants.FLEET_COUNT; i++) {
            Vehicle v = Vehicle.builder()
                    .plateNumber("34D" + String.format("%04d", 9000 + i))
                    .brand(VEHICLE_BRANDS[i])
                    .model(VEHICLE_MODELS[i])
                    .capacity(16 + i * 2)
                    .active(true)
                    .build();
            saved.add(vehicleRepository.save(v));
        }
        return saved;
    }

    private void seedDrivers(String encodedPassword, List<Vehicle> vehicles) {
        for (int i = 0; i < DemoDataConstants.FLEET_COUNT; i++) {
            User user = userRepository.save(User.builder()
                    .email(DemoDataConstants.driverEmail(i))
                    .password(encodedPassword)
                    .firstName(DRIVER_FIRST[i])
                    .lastName(DRIVER_LAST[i])
                    .roles(Set.of(RoleType.ROLE_DRIVER))
                    .active(true)
                    .approvalStatus(ApprovalStatus.APPROVED)
                    .build());

            Driver driver = Driver.builder()
                    .user(user)
                    .phoneNumber("0532" + String.format("%07d", 2000000 + i))
                    .licenseNumber("D-DEMO-" + String.format("%012d", i))
                    .vehicle(vehicles.get(i))
                    .active(true)
                    .build();
            driverRepository.save(driver);
        }
    }

    private void seedHostesses(String encodedPassword) {
        for (int i = 0; i < DemoDataConstants.FLEET_COUNT; i++) {
            User user = userRepository.save(User.builder()
                    .email(DemoDataConstants.hostessEmail(i))
                    .password(encodedPassword)
                    .firstName(HOSTESS_FIRST[i])
                    .lastName(HOSTESS_LAST[i])
                    .roles(Set.of(RoleType.ROLE_HOSTESS))
                    .active(true)
                    .approvalStatus(ApprovalStatus.APPROVED)
                    .build());

            Hostess hostess = Hostess.builder()
                    .user(user)
                    .phone("0533" + String.format("%07d", 3000000 + i))
                    .emergencyContact("0533" + String.format("%07d", 4000000 + i))
                    .active(true)
                    .build();
            hostessRepository.save(hostess);
        }
    }

    private void seedParentsAndStudents(String encodedPassword) {
        for (int i = 0; i < DemoDataConstants.PARENT_STUDENT_COUNT; i++) {
            String pFirst = PARENT_FIRST_NAMES[i % PARENT_FIRST_NAMES.length];
            String pLast = "Veli" + i;

            User parentUser = userRepository.save(User.builder()
                    .email(DemoDataConstants.parentEmail(i))
                    .password(encodedPassword)
                    .firstName(pFirst)
                    .lastName(pLast)
                    .roles(Set.of(RoleType.ROLE_PARENT))
                    .active(true)
                    .approvalStatus(ApprovalStatus.APPROVED)
                    .build());

            Parent parent = Parent.builder()
                    .user(parentUser)
                    .phoneNumber("0534" + String.format("%07d", 5000000 + i))
                    .emergencyContactName("Yakın " + i)
                    .emergencyContactPhone("0535" + String.format("%07d", 6000000 + i))
                    .address("Demo Mah. Okul Cad. No:" + (i + 1))
                    .district("Kadıköy")
                    .city("İstanbul")
                    .identityNumber(String.format("990%08d", i))
                    .build();
            parentRepository.save(parent);

            String sFirst = STUDENT_FIRST_NAMES[i % STUDENT_FIRST_NAMES.length];
            String sLast = "Öğrenci" + i;
            LocalDate birth = LocalDate.of(2010, 1, 1).plusDays(i % 600);
            Gender gender = i % 2 == 0 ? Gender.MALE : Gender.FEMALE;

            Student student = Student.builder()
                    .firstName(sFirst)
                    .lastName(sLast)
                    .birthDate(birth)
                    .gender(gender)
                    .schoolName("Demo İlkokulu")
                    .grade((i % 8 + 1) + ". Sınıf")
                    .className("Şube " + (char) ('A' + (i % 4)))
                    .address("Öğrenci Adresi " + (i + 1) + ", Kadıköy")
                    .district("Kadıköy")
                    .city("İstanbul")
                    .parent(parent)
                    .active(true)
                    .build();
            studentRepository.save(student);
        }
    }
}
