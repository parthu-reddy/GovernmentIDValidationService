package com.fooddelivery.contract;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.openfeign.EnableFeignClients;

@ActiveProfiles("contract-test")
@SpringBootTest(classes = GovIdContractConsumerTest.TestConfig.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureStubRunner(ids = { "com.fooddelivery:restaurant-application:+:stubs:8091", "com.fooddelivery:delivery-executive-application:+:stubs:8092" }, stubsMode = StubRunnerProperties.StubsMode.LOCAL)
public class GovIdContractConsumerTest {


    @Autowired
    private com.fooddelivery.governmentid.client.DeliveryExecutiveClient deliveryExecutiveClient;
    @Autowired
    private com.fooddelivery.governmentid.client.RestaurantServiceClient restaurantServiceClient;


    @Configuration
    @EnableAutoConfiguration(exclude = {
            DataSourceAutoConfiguration.class,
            DataSourceTransactionManagerAutoConfiguration.class,
            HibernateJpaAutoConfiguration.class
    })
    @EnableFeignClients(basePackages = "com.fooddelivery.governmentid.client")
    @org.springframework.context.annotation.Import({com.fooddelivery.governmentid.client.DeliveryExecutiveClientFallback.class, com.fooddelivery.governmentid.client.RestaurantServiceClientFallback.class})
    static class TestConfig {
    }

    @Test
    public void contextLoads() {
        assertNotNull(deliveryExecutiveClient);
        assertNotNull(restaurantServiceClient);
}
}
