package com.fooddelivery.governmentid.client;

import org.springframework.stereotype.Component;
import org.springframework.http.ResponseEntity;

@Component("governmentidvalidationDeliveryExecutiveClientFallback")
public class DeliveryExecutiveClientFallback implements DeliveryExecutiveClient {
    @Override
    public ResponseEntity<Void> suspendDriver(String driverId) {
        throw new IllegalStateException("Delivery service is currently unavailable.");
    }
}
