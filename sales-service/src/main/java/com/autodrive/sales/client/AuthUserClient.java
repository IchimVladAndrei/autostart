package com.autodrive.sales.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Component
public class AuthUserClient {

    private final RestClient restClient;
    private final String internalToken;

    public AuthUserClient(@Value("${app.services.auth-user-url}") String baseUrl,
                          @Value("${app.internal.token}") String internalToken) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
        this.internalToken = internalToken;
    }

    public boolean existsCustomer(UUID customerId) {
        Boolean exists = restClient.get()
                .uri("/api/internal/customers/{id}/exists", customerId)
                .header("X-Internal-Token", internalToken)
                .retrieve()
                .body(Boolean.class);
        return Boolean.TRUE.equals(exists);
    }

    public boolean existsEmployee(UUID employeeId) {
        Boolean exists = restClient.get()
                .uri("/api/internal/employees/{id}/exists", employeeId)
                .header("X-Internal-Token", internalToken)
                .retrieve()
                .body(Boolean.class);
        return Boolean.TRUE.equals(exists);
    }
}
