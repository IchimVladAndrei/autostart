package com.autodrive.backend.service;

import com.autodrive.backend.dto.user.CustomerCreateRequest;
import com.autodrive.backend.dto.user.UserCreateRequest;
import com.autodrive.backend.dto.vehicle.ExtraOptionCreateRequest;
import com.autodrive.backend.dto.vehicle.VehicleCreateRequest;
import com.autodrive.backend.entity.user.CustomerStatus;
import com.autodrive.backend.entity.user.User;
import com.autodrive.backend.entity.user.UserRole;
import com.autodrive.backend.entity.vehicle.Brand;
import com.autodrive.backend.entity.vehicle.ExtraOption;
import com.autodrive.backend.entity.vehicle.VehicleStatus;
import com.autodrive.backend.exception.BadRequestException;
import com.autodrive.backend.exception.ConflictException;
import com.autodrive.backend.exception.DuplicateResourceException;
import com.autodrive.backend.repo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class CrudServiceIntegrationTest {
    @Autowired
    private UserService userService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private ExtraOptionService extraOptionService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private ExtraOptionRepository extraOptionRepository;
    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    void cleanDatabase() {
        paymentRepository.deleteAll();
        saleRepository.deleteAll();
        vehicleRepository.deleteAll();
        extraOptionRepository.deleteAll();
        brandRepository.deleteAll();
        customerRepository.deleteAll();
        employeeRepository.deleteAll();
        refreshTokenRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void userCreationEncodesPasswordAndRejectsDuplicateEmail() {
        userService.create(new UserCreateRequest(
                "user@example.com",
                "First",
                "Last",
                "0711111111",
                "secret1",
                UserRole.ADMIN
        ));

        User saved = userRepository.findByEmail("user@example.com").orElseThrow();
        assertTrue(passwordEncoder.matches("secret1", saved.getPassword()));
        assertFalse(saved.getPassword().equals("secret1"));

        assertThrows(DuplicateResourceException.class, () -> userService.create(new UserCreateRequest(
                "user@example.com",
                "Other",
                "Person",
                "0711111112",
                "secret1",
                UserRole.USER
        )));
    }

    @Test
    void customerCreationUsesExistingUserAndRejectsDuplicateProfile() {
        User user = userRepository.save(User.builder()
                .email("customer@example.com")
                .firstName("Customer")
                .lastName("User")
                .phone("0711111111")
                .password(passwordEncoder.encode("secret1"))
                .role(UserRole.USER)
                .build());

        customerService.create(new CustomerCreateRequest(
                user.getId(),
                "Customer User",
                "1234567890123",
                "0711111111",
                CustomerStatus.ACTIVE
        ));

        assertTrue(customerRepository.existsById(user.getId()));
        assertThrows(ConflictException.class, () -> customerService.create(new CustomerCreateRequest(
                user.getId(),
                "Customer Again",
                "1234567890124",
                "0711111112",
                CustomerStatus.ACTIVE
        )));
    }

    @Test
    void vehicleCreationRejectsDuplicateOptionIds() {
        Brand brand = brandRepository.save(Brand.builder()
                .name("Honda")
                .country("Japan")
                .foundedYear(1948)
                .build());
        ExtraOption option = extraOptionRepository.save(ExtraOption.builder()
                .name("Heated seats")
                .description("Front seats")
                .price(new BigDecimal("500.00"))
                .build());

        VehicleCreateRequest request = new VehicleCreateRequest(
                "WVWZZZ1JZXW000002",
                "Golf",
                new BigDecimal("10000.00"),
                2020,
                VehicleStatus.AVAILABLE,
                "Black",
                0,
                brand.getId(),
                List.of(option.getId(), option.getId())
        );

        assertThrows(BadRequestException.class, () -> vehicleService.create(request));
    }

    @Test
    void extraOptionDeletionRejectsAssignedOptions() {
        Brand brand = brandRepository.save(Brand.builder()
                .name("Ford")
                .country("USA")
                .foundedYear(1903)
                .build());
        ExtraOption option = extraOptionRepository.save(ExtraOption.builder()
                .name("Tow package")
                .description("Tow")
                .price(new BigDecimal("700.00"))
                .build());
        vehicleService.create(new VehicleCreateRequest(
                "WVWZZZ1JZXW000003",
                "Focus",
                new BigDecimal("9000.00"),
                2021,
                VehicleStatus.AVAILABLE,
                "White",
                0,
                brand.getId(),
                List.of(option.getId())
        ));

        assertThrows(ConflictException.class, () -> extraOptionService.delete(option.getId()));
    }
}
