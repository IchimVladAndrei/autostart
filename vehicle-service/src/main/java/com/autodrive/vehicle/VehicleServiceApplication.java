package com.autodrive.vehicle;

import com.autodrive.common.config.DotenvLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication(scanBasePackages = {"com.autodrive.vehicle", "com.autodrive.common"})
@ConfigurationPropertiesScan(basePackages = {"com.autodrive.common"})
public class VehicleServiceApplication {

    public static void main(String[] args) {
        DotenvLoader.load("vehicle-service");
        SpringApplication.run(VehicleServiceApplication.class, args);
    }
}
