package com.fooddelivery.governmentid.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.UUID;
import lombok.Data;

@FeignClient(name = "restaurant-service", url = "${restaurant.base.url:http://localhost:8081}")
public interface RestaurantServiceClient {

    @PostMapping("/api/v1/internal/brands/{brandId}/verification-callback")
    ResponseEntity<Void> updateVerificationStatus(@PathVariable("brandId") UUID brandId, @RequestBody VerificationCallbackRequest request);
    
    @Data
    class VerificationCallbackRequest {
        private String verificationType; // GSTIN or PENNY_DROP
        private String status; // VERIFIED, REJECTED, FAILED
        private String legalEntityName;
        private String bankBeneficiaryName;
        private Double matchScore;
    }
}
