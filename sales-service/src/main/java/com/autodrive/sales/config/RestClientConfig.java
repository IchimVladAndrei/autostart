package com.autodrive.sales.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    //folosit de Eureka
    @Bean
    @Primary
    public RestClient.Builder normalRestClientBuilder() {
        return RestClient.builder();
    }

    // folosit pt noi
    @Bean("loadBalancedBuilder")
    @LoadBalanced
    public RestClient.Builder loadBalancedRestClientBuilder() {
        return RestClient.builder();
    }
}