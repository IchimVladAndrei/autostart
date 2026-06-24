package com.autodrive.vehicle;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootApplication(scanBasePackages = {"com.autodrive.vehicle", "com.autodrive.common"})
@ConfigurationPropertiesScan(basePackages = {"com.autodrive.common"})
public class VehicleServiceApplication {

    public static void main(String[] args) {
        loadDotenv();
        SpringApplication.run(VehicleServiceApplication.class, args);
    }

    private static void loadDotenv() {
        String directory = ".";
        if (Files.exists(Path.of("vehicle-service", ".env"))) {
            directory = "vehicle-service";
        } else if (Files.exists(Path.of("autostart", ".env"))) {
            directory = "autostart";
        }
        Dotenv dotenv = Dotenv.configure()
                .directory(directory)
                .ignoreIfMissing()
                .load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
    }
}
