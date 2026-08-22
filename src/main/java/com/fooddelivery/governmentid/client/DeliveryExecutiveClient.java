package com.fooddelivery.governmentid.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(name = "delivery-service", fallback = DeliveryExecutiveClientFallback.class)
public interface DeliveryExecutiveClient {

    @PostMapping("/api/v1/internal/delivery/drivers/{driverId}/suspend")
    ResponseEntity<Void> suspendDriver(@org.springframework.web.bind.annotation.PathVariable("driverId") String driverId);
}
