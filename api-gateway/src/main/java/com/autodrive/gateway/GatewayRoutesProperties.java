package com.autodrive.gateway;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.routes")
public record GatewayRoutesProperties(String authUserUrl, String vehicleUrl, String salesUrl) {
}
