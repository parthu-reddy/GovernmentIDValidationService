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
@SpringBootTest(classes = GovIdContractConsumerTest.TestConfig.class, webEnvironment = SpringBootTest.WebEnvironment.NONE, properties = {
    // Stub ids are Maven artifactIds; Feign resolves by spring.application.name. These two
    // differ for these services, so the stub must be registered under the name the client asks for.
    "stubrunner.idsToServiceIds.restaurant-application=restaurant-service",
    "stubrunner.idsToServiceIds.delivery-executive-application=delivery-service"
})
@AutoConfigureStubRunner(ids = { "com.fooddelivery:restaurant-application:+:stubs", "com.fooddelivery:delivery-executive-application:+:stubs" }, stubsMode = StubRunnerProperties.StubsMode.LOCAL)
public class GovIdContractConsumerTest {


    @Autowired
    private com.fooddelivery.governmentid.client.DeliveryExecutiveClient deliveryExecutiveClient;

    @org.springframework.boot.SpringBootConfiguration
    @org.springframework.boot.autoconfigure.EnableAutoConfiguration(exclude = {
            DataSourceAutoConfiguration.class,
            DataSourceTransactionManagerAutoConfiguration.class,
            HibernateJpaAutoConfiguration.class
    })
    @EnableFeignClients(basePackages = "com.fooddelivery.governmentid.client")
    @org.springframework.context.annotation.Import({com.fooddelivery.governmentid.client.DeliveryExecutiveClientFallback.class})
    static class TestConfig {
    }

    @Test
    public void testSuspendDriver() {
        org.springframework.http.ResponseEntity<Void> response = deliveryExecutiveClient.suspendDriver("00000000-0000-0000-0000-000000000000");
        assertNotNull(response);
        org.junit.jupiter.api.Assertions.assertEquals(200, response.getStatusCodeValue());
    }
}
