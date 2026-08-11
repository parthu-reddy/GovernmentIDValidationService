package com.fooddelivery.governmentid.client;

import org.springframework.stereotype.Component;
import org.springframework.http.ResponseEntity;
import java.util.UUID;

@Component
public class RestaurantServiceClientFallback implements RestaurantServiceClient {
    @Override
    public ResponseEntity<Void> updateVerificationStatus(UUID brandId, VerificationCallbackRequest request) {
        throw new IllegalStateException("Restaurant service is currently unavailable.");
    }
}
