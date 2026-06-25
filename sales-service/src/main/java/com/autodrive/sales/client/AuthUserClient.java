package com.autodrive.sales.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Optional;
import java.util.UUID;

@Component
@RefreshScope
public class AuthUserClient {

    private final WebClient webClient;
    private final String internalToken;

    public AuthUserClient(@Value("${app.services.auth-user-url}") String baseUrl,
                          @Value("${app.internal.token}") String internalToken,
                          WebClient.Builder loadBalancedWebClientBuilder) {
        this.webClient = loadBalancedWebClientBuilder.baseUrl(baseUrl).build();
        this.internalToken = internalToken;
    }

    public boolean existsCustomer(UUID customerId) {
        Boolean exists = getBooleanWithFailover("/api/internal/customers/{id}/exists", customerId);
        return Boolean.TRUE.equals(exists);
    }

    public boolean existsEmployee(UUID employeeId) {
        Boolean exists = getBooleanWithFailover("/api/internal/employees/{id}/exists", employeeId);
        return Boolean.TRUE.equals(exists);
    }

    public Optional<UUID> findCustomerIdByEmail(String email) {
        try {
            return getCustomerId(email);
        } catch (WebClientRequestException firstFailure) {
            return getCustomerId(email);
        } catch (WebClientResponseException.NotFound ex) {
            return Optional.empty();
        }
    }

    private Optional<UUID> getCustomerId(String email) {
        try {
            UUID customerId = webClient.get()
                    .uri("/api/internal/customers/by-email/{email}/id", email)
                    .header("X-Internal-Token", internalToken)
                    .retrieve()
                    .bodyToMono(UUID.class)
                    .block();
            return Optional.ofNullable(customerId);
        } catch (WebClientResponseException.NotFound ex) {
            return Optional.empty();
        }
    }

    private Boolean getBooleanWithFailover(String uri, Object value) {
        try {
            return getBoolean(uri, value);
        } catch (WebClientRequestException firstFailure) {
            return getBoolean(uri, value);
        }
    }

    private Boolean getBoolean(String uri, Object value) {
        return webClient.get()
                .uri(uri, value)
                .header("X-Internal-Token", internalToken)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
    }
}
