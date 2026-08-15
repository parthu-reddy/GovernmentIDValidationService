package com.fooddelivery.governmentid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = {"com.fooddelivery.governmentid", "com.fooddelivery.common", "com.fooddelivery"})
@EnableDiscoveryClient
@EnableFeignClients
@EnableJpaRepositories(basePackages = {"com.fooddelivery.governmentid", "com.fooddelivery.common"})
@org.springframework.boot.autoconfigure.domain.EntityScan(basePackages = {"com.fooddelivery.governmentid", "com.fooddelivery.common"})
@EnableAsync
public class GovernmentIDValidationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GovernmentIDValidationServiceApplication.class, args);
    }
}
