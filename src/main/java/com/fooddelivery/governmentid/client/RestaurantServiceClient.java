package com.fooddelivery.governmentid.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.UUID;

@FeignClient(name = "restaurant-service", url = "${restaurant.base.url:}", fallback = RestaurantServiceClientFallback.class)
public interface RestaurantServiceClient {
    @PostMapping("/api/v1/internal/brands/{brandId}/verification-callback")
    ResponseEntity<Void> updateVerificationStatus(@PathVariable("brandId") UUID brandId, @RequestBody VerificationCallbackRequest request);

    @lombok.AllArgsConstructor
    @lombok.NoArgsConstructor
    @lombok.Data
    class VerificationCallbackRequest {
        private String verificationType; // GSTIN or PENNY_DROP
        private String status; // VERIFIED, REJECTED, FAILED
        private String legalEntityName;
        private String bankBeneficiaryName;
        private Double matchScore;
    }
}
