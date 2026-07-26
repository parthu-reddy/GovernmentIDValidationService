package com.fooddelivery.governmentid.controller;

import com.fooddelivery.governmentid.service.BrandVerificationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import lombok.Data;

@Slf4j
@RestController
@RequestMapping("/api/v1/verification/brands")
@RequiredArgsConstructor
public class BrandVerificationController {

    private final BrandVerificationService brandVerificationService;

    @PostMapping("/gstin")
    public ResponseEntity<Void> verifyGstin(@Valid @RequestBody GstinRequest request) {
        brandVerificationService.verifyGstin(request.getBrandId(), request.getGstin(), request.getBrandName());
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/bank-account")
    public ResponseEntity<Void> verifyBankAccount(@Valid @RequestBody BankAccountRequest request) {
        brandVerificationService.initiatePennyDrop(request.getBrandId(), request.getAccountNumber(), request.getIfscCode(), request.getBrandName());
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/webhooks/penny-drop")
    public ResponseEntity<Void> handlePennyDropWebhook(@Valid @RequestBody PennyDropWebhook request) {
        brandVerificationService.processPennyDropWebhook(
                request.getBrandId(), 
                request.getBeneficiaryName(), 
                "SUCCESS".equalsIgnoreCase(request.getStatus()),
                request.getRegisteredBrandName()
        );
        return ResponseEntity.ok().build();
    }
    
    @Data
    public static class GstinRequest {
        private UUID brandId;
        @NotBlank
        private String gstin;
        @NotBlank
        private String brandName;
    }

    @Data
    public static class BankAccountRequest {
        private UUID brandId;
        @NotBlank
        private String accountNumber;
        @NotBlank
        private String ifscCode;
        @NotBlank
        private String brandName;
    }

    @Data
    public static class PennyDropWebhook {
        private UUID brandId;
        private String beneficiaryName;
        private String status;
        private String registeredBrandName; // Passed in webhook or fetched if we saved it initially
    }
}
