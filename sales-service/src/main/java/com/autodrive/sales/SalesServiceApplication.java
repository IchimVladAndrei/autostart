package com.autodrive.sales;

import com.autodrive.common.config.DotenvLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication(scanBasePackages = {"com.autodrive.sales", "com.autodrive.common"})
@ConfigurationPropertiesScan(basePackages = {"com.autodrive.common"})
public class SalesServiceApplication {

    public static void main(String[] args) {
        DotenvLoader.load("sales-service");
        SpringApplication.run(SalesServiceApplication.class, args);
    }
}
