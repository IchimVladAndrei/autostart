package com.autodrive.authuser;

import com.autodrive.common.config.DotenvLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication(scanBasePackages = {"com.autodrive.authuser", "com.autodrive.common"})
@ConfigurationPropertiesScan(basePackages = {"com.autodrive.common"})
public class AuthUserServiceApplication {

    public static void main(String[] args) {
        DotenvLoader.load("auth-user-service");
        SpringApplication.run(AuthUserServiceApplication.class, args);
    }
}
