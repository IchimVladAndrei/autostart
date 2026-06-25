package com.autodrive.sales.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Component
public class AuthUserClient {

    private final RestClient restClient;
    private final String internalToken;

    public AuthUserClient(@Qualifier("loadBalancedBuilder") RestClient.Builder builder,
                          @Value("${app.internal.token}") String internalToken) {
        this.restClient = builder.baseUrl("http://auth-user-service").build();
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
