package com.autodrive.sales.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class VehicleClient {

    private final RestClient restClient;
    private final String internalToken;

    public VehicleClient(@Value("${app.services.vehicle-url}") String baseUrl,
                         @Value("${app.internal.token}") String internalToken) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
        this.internalToken = internalToken;
    }

    public boolean existsVehicle(String vin) {
        Boolean exists = restClient.get()
                .uri("/api/internal/vehicles/{vin}/exists", vin)
                .header("X-Internal-Token", internalToken)
                .retrieve()
                .body(Boolean.class);
        return Boolean.TRUE.equals(exists);
    }

    public void updateVehicleStatus(String vin, String status) {
        restClient.patch()
                .uri("/api/internal/vehicles/{vin}/status", vin)
                .header("X-Internal-Token", internalToken)
                .body(Map.of("status", status))
                .retrieve()
                .toBodilessEntity();
    }
}
