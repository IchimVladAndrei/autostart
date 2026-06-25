package com.autodrive.configserver;

import com.autodrive.common.config.DotenvLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

import java.util.Map;

@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {
    public static void main(String[] args) {
        DotenvLoader.load("config-server");
        configureSharedOverrides();
        SpringApplication.run(ConfigServerApplication.class, args);
    }

    private static void configureSharedOverrides() {
        Map<String, String> sharedOverrides = Map.of(
                "app.jwt.secret", requiredConfig("JWT_SECRET"),
                "app.internal.token", requiredConfig("INTERNAL_SERVICE_TOKEN"),
                "spring.datasource.url", requiredConfig("DB_URL"),
                "spring.datasource.username", requiredConfig("DB_USER"),
                "spring.datasource.password", requiredConfig("DB_PASSWORD")
        );

        sharedOverrides.forEach((key, value) ->
                System.setProperty("spring.cloud.config.server.overrides." + key, value)
        );
    }

    private static String requiredConfig(String name) {
        String value = System.getProperty(name);
        if (value == null || value.isBlank()) {
            value = System.getenv(name);
        }
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(name + " must be configured for Config Server");
        }
        return value;
    }
}
