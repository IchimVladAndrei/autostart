package com.autodrive.sales.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import java.util.Map;

@Component
public class VehicleClient {

    private final WebClient webClient;
    private final String internalToken;

    public VehicleClient(@Value("${app.services.vehicle-url}") String baseUrl,
                         @Value("${app.internal.token}") String internalToken,
                         WebClient.Builder loadBalancedWebClientBuilder) {
        this.webClient = loadBalancedWebClientBuilder.baseUrl(baseUrl).build();
        this.internalToken = internalToken;
    }

    public boolean existsVehicle(String vin) {
        Boolean exists = getBooleanWithFailover("/api/internal/vehicles/{vin}/exists", vin);
        return Boolean.TRUE.equals(exists);
    }

    public void updateVehicleStatus(String vin, String status) {
        try {
            patchStatus(vin, status);
        } catch (WebClientRequestException firstFailure) {
            patchStatus(vin, status);
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

    private void patchStatus(String vin, String status) {
        webClient.patch()
                .uri("/api/internal/vehicles/{vin}/status", vin)
                .header("X-Internal-Token", internalToken)
                .bodyValue(Map.of("status", status))
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
