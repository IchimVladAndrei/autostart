package com.autodrive.sales;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootApplication(scanBasePackages = {"com.autodrive.sales", "com.autodrive.common"})
@ConfigurationPropertiesScan(basePackages = {"com.autodrive.common"})
@EnableFeignClients
public class SalesServiceApplication {

    public static void main(String[] args) {
        loadDotenv();
        SpringApplication.run(SalesServiceApplication.class, args);
    }

    private static void loadDotenv() {
        String directory = ".";
        if (Files.exists(Path.of("sales-service", ".env"))) {
            directory = "sales-service";
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
