package com.autodrive.backend.security;

import com.autodrive.backend.entity.sale.SaleContract;
import com.autodrive.backend.entity.sale.SaleContractStatus;
import com.autodrive.backend.entity.user.*;
import com.autodrive.backend.entity.vehicle.Brand;
import com.autodrive.backend.entity.vehicle.Vehicle;
import com.autodrive.backend.entity.vehicle.VehicleStatus;
import com.autodrive.backend.repo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityIntegrationTest {
    private int cnpCounter;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private ExtraOptionRepository extraOptionRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    void cleanDatabase() {
        cnpCounter = 100;
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
    void publicRegisterIsAccessibleAndAnonymousProtectedReadIsRejected() throws Exception {
        mockMvc.perform(post("/api/v1/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"register@example.com","firstName":"Reg","lastName":"User","phone":"0711111111","password":"secret1"}
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/brands"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void csrfTokenEndpointIsPublic() throws Exception {
        mockMvc.perform(get("/api/v1/auth/csrf"))
                .andExpect(status().isOk());
    }

    @Test
    void repeatedLoginRotatesExistingRefreshToken() throws Exception {
        userRepository.save(User.builder()
                .email("repeat-login@example.com")
                .firstName("Repeat")
                .lastName("Login")
                .phone("0711111111")
                .password(passwordEncoder.encode("secret1"))
                .role(UserRole.ADMIN)
                .build());

        String body = """
                {"email":"repeat-login@example.com","password":"secret1","rememberMe":true}
                """;

        mockMvc.perform(post("/api/v1/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty());

        mockMvc.perform(post("/api/v1/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty());
    }

    @Test
    void localFrontendCorsPreflightIsAllowed() throws Exception {
        mockMvc.perform(options("/api/v1/brands")
                        .header(HttpHeaders.ORIGIN, "http://localhost:3000")
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET")
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, "authorization"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:3000"))
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "authorization"));
    }

    @Test
    void authenticatedCatalogReadsReachControllersAndUserCannotWrite() throws Exception {
        String token = tokenFor(UserRole.USER, null);

        mockMvc.perform(get("/api/v1/brands").header(HttpHeaders.AUTHORIZATION, bearer(token)))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/v1/extra-options").header(HttpHeaders.AUTHORIZATION, bearer(token)))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/v1/vehicles").header(HttpHeaders.AUTHORIZATION, bearer(token)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/brands")
                        .with(csrf())
                        .header(HttpHeaders.AUTHORIZATION, bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Toyota","country":"Japan","foundedYear":1937}
                                """))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminAndManagerCanWriteCatalogResources() throws Exception {
        String adminToken = tokenFor(UserRole.ADMIN, null);
        String managerToken = tokenFor(UserRole.USER, EmployeePosition.MANAGER);

        mockMvc.perform(post("/api/v1/brands")
                        .with(csrf())
                        .header(HttpHeaders.AUTHORIZATION, bearer(adminToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Mazda","country":"Japan","foundedYear":1920}
                                """))
                .andExpect(status().isCreated());

        UUID brandId = brandRepository.findAll().get(0).getId();
        mockMvc.perform(post("/api/v1/vehicles")
                        .with(csrf())
                        .header(HttpHeaders.AUTHORIZATION, bearer(managerToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"vin":"WVWZZZ1JZXW000001","model":"Golf","basePrice":10000.00,"year":2020,"status":"AVAILABLE","color":"Black","mileage":0,"brandId":"%s","extraOptionIds":[]}
                                """.formatted(brandId)))
                .andExpect(status().isCreated());
    }

    @Test
    void salesConsultantCanCreateContractsAndFinanceSpecialistCanCreatePayments() throws Exception {
        String salesToken = tokenFor(UserRole.USER, EmployeePosition.SALES_CONSULTANT);
        String financeToken = tokenFor(UserRole.USER, EmployeePosition.FINANCE_SPECIALIST);
        TestSaleData data = createSaleData();

        mockMvc.perform(post("/api/v1/sale-contracts")
                        .with(csrf())
                        .header(HttpHeaders.AUTHORIZATION, bearer(salesToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"contractDate":"2026-01-01T10:00:00","salePrice":12000.00,"notes":"ok","status":"PENDING","vehicleVin":"%s","customerId":"%s","employeeId":"%s"}
                                """.formatted(data.vehicleVin(), data.customerId(), data.employeeId())))
                .andExpect(status().isCreated());

        UUID contractId = saleRepository.findAll().get(0).getId();
        mockMvc.perform(post("/api/v1/payments")
                        .with(csrf())
                        .header(HttpHeaders.AUTHORIZATION, bearer(financeToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"amount":1000.00,"type":"CASH","status":"ACCEPTED","paymentDate":"2026-01-02T10:00:00","contractId":"%s"}
                                """.formatted(contractId)))
                .andExpect(status().isCreated());
    }

    private String tokenFor(UserRole role, EmployeePosition position) {
        String unique = UUID.randomUUID().toString().substring(0, 8);
        User user = userRepository.save(User.builder()
                .email(unique + "@example.com")
                .firstName("Test")
                .lastName("User")
                .phone("0711111111")
                .password(passwordEncoder.encode("secret1"))
                .role(role)
                .build());
        if (position != null) {
            employeeRepository.save(Employee.builder()
                    .user(user)
                    .name("Employee " + unique)
                    .position(position)
                    .cnp(cnp())
                    .status("ACTIVE")
                    .baseSalary(new BigDecimal("1000.00"))
                    .bonus(BigDecimal.ZERO)
                    .hireDate(LocalDate.of(2020, 1, 1))
                    .build());
        }
        return jwtService.generateAccessToken(user);
    }

    private TestSaleData createSaleData() {
        User customerUser = userRepository.save(User.builder()
                .email("customer-" + UUID.randomUUID() + "@example.com")
                .firstName("Customer")
                .lastName("One")
                .phone("0722222222")
                .password(passwordEncoder.encode("secret1"))
                .role(UserRole.USER)
                .build());
        Customer customer = customerRepository.save(Customer.builder()
                .user(customerUser)
                .name("Customer One")
                .cnp(cnp())
                .phone("0722222222")
                .status(CustomerStatus.ACTIVE)
                .build());

        User employeeUser = userRepository.save(User.builder()
                .email("seller-" + UUID.randomUUID() + "@example.com")
                .firstName("Seller")
                .lastName("One")
                .phone("0733333333")
                .password(passwordEncoder.encode("secret1"))
                .role(UserRole.USER)
                .build());
        Employee employee = employeeRepository.save(Employee.builder()
                .user(employeeUser)
                .name("Seller One")
                .position(EmployeePosition.SALES_CONSULTANT)
                .cnp(cnp())
                .status("ACTIVE")
                .baseSalary(new BigDecimal("1000.00"))
                .bonus(BigDecimal.ZERO)
                .hireDate(LocalDate.of(2020, 1, 1))
                .build());

        Brand brand = brandRepository.save(Brand.builder()
                .name("Brand " + UUID.randomUUID())
                .country("Japan")
                .foundedYear(1930)
                .build());
        Vehicle vehicle = vehicleRepository.save(Vehicle.builder()
                .vin("1HGCM82633A" + UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase())
                .model("Civic")
                .basePrice(new BigDecimal("12000.00"))
                .year(2020)
                .status(VehicleStatus.AVAILABLE)
                .color("Blue")
                .mileage(10)
                .brand(brand)
                .build());
        return new TestSaleData(vehicle.getVin(), customer.getUserId(), employee.getUserId());
    }

    private String bearer(String token) {
        return "Bearer " + token;
    }

    private String cnp() {
        return "1" + String.format("%012d", cnpCounter++);
    }

    private record TestSaleData(String vehicleVin, UUID customerId, UUID employeeId) {
    }
}
