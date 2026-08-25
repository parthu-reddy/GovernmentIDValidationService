package com.fooddelivery.governmentid;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.context.WebApplicationContext;

import org.springframework.boot.test.mock.mockito.MockBean;
import com.fooddelivery.common.service.RateLimitingService;

@SpringBootTest(properties = "spring.main.allow-bean-definition-overriding=true", classes = GovernmentIDValidationServiceApplication.class)
public abstract class ContractTestBase {

    @MockBean
    private RateLimitingService rateLimitingService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }
}
