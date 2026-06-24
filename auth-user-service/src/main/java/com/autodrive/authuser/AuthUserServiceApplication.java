package com.autodrive.authuser;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootApplication(scanBasePackages = {"com.autodrive.authuser", "com.autodrive.common"})
@ConfigurationPropertiesScan(basePackages = {"com.autodrive.common"})
public class AuthUserServiceApplication {

    public static void main(String[] args) {
        loadDotenv();
        SpringApplication.run(AuthUserServiceApplication.class, args);
    }

    private static void loadDotenv() {
        String directory = ".";
        if (Files.exists(Path.of("auth-user-service", ".env"))) {
            directory = "auth-user-service";
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
