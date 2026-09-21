package com.fooddelivery.governmentid.controller;

import com.fooddelivery.governmentid.entity.DocumentType;
import com.fooddelivery.common.enums.VerificationStatus;
import com.fooddelivery.governmentid.service.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/verification")
@lombok.extern.slf4j.Slf4j
public class VerificationController {
    @java.lang.SuppressWarnings("all")

    private final DrivingLicenseVerificationService dlService;
    private final VehicleVerificationService rcService;
    private final FinancialVerificationService bankService;
    private final BiometricVerificationService biometricService;
    private final ExecutiveDocumentService documentService;
    private final com.fooddelivery.common.service.CloudflareR2Service storageService;
    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper;
    private final com.fooddelivery.common.service.RateLimitingService rateLimitingService;
    public record DLRequest(@NotBlank String dlNumber, @NotBlank String dateOfBirth, String documentUrl) {
    }


    public record RCRequest(@NotBlank String registrationNumber, String documentUrl) {
    }


    public record BankRequest(@NotBlank String accountNumber, @NotBlank String ifscCode, @NotBlank String kycFullName) {
    }


    public record BiometricRequest(@NotBlank String selfieUrl) {
    }

    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    @GetMapping("/upload-url")
    @PreAuthorize("hasAnyRole('DELIVERY', 'RESTAURANT', 'RESTAURANT_MANAGER')")
    public ResponseEntity<?> getPresignedUploadUrl(@RequestParam DocumentType docType, @RequestParam String contentType, Principal principal) {
        UUID executiveId = principal != null ? UUID.fromString(principal.getName()) : UUID.randomUUID();
        String ext = contentType.contains("pdf") ? "pdf" : "jpg";
        String objectKey = "documents/" + executiveId + "/" + docType.name() + "_" + UUID.randomUUID() + "." + ext;
        if ("dev".equalsIgnoreCase(activeProfile) || "test".equalsIgnoreCase(activeProfile)) {
            return ResponseEntity.ok(java.util.Map.of("uploadUrl", "http://localhost:8080/mock-upload-url/" + objectKey, "objectKey", objectKey));
        }
        java.net.URL url = storageService.generatePresignedUploadUrl(objectKey, contentType, java.time.Duration.ofMinutes(15));
        return ResponseEntity.ok(java.util.Map.of("uploadUrl", url.toString(), "objectKey", objectKey));
    }

    @GetMapping("/download-url")
    @PreAuthorize("hasRole('DELIVERY')")
    public ResponseEntity<?> getPresignedDownloadUrl(@RequestParam String objectKey, Principal principal) {
        // Basic security check: ensure the object key belongs to this executive
        UUID executiveId = UUID.fromString(principal.getName());
        if (!objectKey.contains(executiveId.toString())) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.FORBIDDEN).body(java.util.Map.of("error", "Access denied"));
        }
        java.net.URL url = storageService.generatePresignedDownloadUrl(objectKey, java.time.Duration.ofMinutes(15));
        return ResponseEntity.ok(java.util.Map.of("downloadUrl", url.toString()));
    }

    @PostMapping("/driving-license")
    @PreAuthorize("hasRole('DELIVERY')")
    public ResponseEntity<?> verifyDrivingLicense(@Valid @RequestBody DLRequest request, Principal principal) {
        UUID executiveId = UUID.fromString(principal.getName());
        io.github.bucket4j.Bucket bucket = rateLimitingService.resolveBucket("verify_dl:" + executiveId, 3, 3, java.time.Duration.ofHours(1));
        if (!bucket.tryConsume(1)) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.TOO_MANY_REQUESTS).build();
        }
        var response = dlService.verifyDrivingLicense(request.dlNumber(), request.dateOfBirth());
        LocalDate expiry = null;
        if (response.expiryDate() != null) {
            try {
                // Typical mock API format
                expiry = LocalDate.parse(response.expiryDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            } catch (DateTimeParseException e) {
                log.warn("Failed to parse DL expiry date: {}", response.expiryDate());
            }
        }
        VerificationStatus status = response.isValid() ? VerificationStatus.APPROVED : VerificationStatus.REJECTED;
        // Persist document result
        String jsonResponse = "";
        try {
            jsonResponse = objectMapper.writeValueAsString(response);
        } catch (Exception e) {
            log.warn("Failed to serialize DL response", e);
            jsonResponse = response.toString();
        }
        documentService.recordVerificationResult(executiveId, DocumentType.DRIVING_LICENSE, request.dlNumber(), request.documentUrl(), jsonResponse, expiry, status);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/vehicle-rc")
    @PreAuthorize("hasRole('DELIVERY')")
    public ResponseEntity<?> verifyVehicleRC(@Valid @RequestBody RCRequest request, Principal principal) {
        UUID executiveId = UUID.fromString(principal.getName());
        io.github.bucket4j.Bucket bucket = rateLimitingService.resolveBucket("verify_rc:" + executiveId, 3, 3, java.time.Duration.ofHours(1));
        if (!bucket.tryConsume(1)) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.TOO_MANY_REQUESTS).build();
        }
        var response = rcService.verifyVehicleRC(request.registrationNumber());
        LocalDate expiry = null;
        if (response.insuranceExpiry() != null) {
            try {
                expiry = LocalDate.parse(response.insuranceExpiry(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            } catch (DateTimeParseException e) {
                log.warn("Failed to parse RC expiry date: {}", response.insuranceExpiry());
            }
        }
        VerificationStatus status = response.isValid() ? VerificationStatus.APPROVED : VerificationStatus.REJECTED;
        String jsonResponse = "";
        try {
            jsonResponse = objectMapper.writeValueAsString(response);
        } catch (Exception e) {
            log.warn("Failed to serialize RC response", e);
            jsonResponse = response.toString();
        }
        documentService.recordVerificationResult(executiveId, DocumentType.RC, request.registrationNumber(), request.documentUrl(), jsonResponse, expiry, status);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/bank-account")
    @PreAuthorize("hasRole('DELIVERY')")
    public ResponseEntity<?> verifyBankAccount(@Valid @RequestBody BankRequest request, Principal principal) {
        UUID executiveId = UUID.fromString(principal.getName());
        io.github.bucket4j.Bucket bucket = rateLimitingService.resolveBucket("verify_bank:" + executiveId, 3, 3, java.time.Duration.ofHours(1));
        if (!bucket.tryConsume(1)) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.TOO_MANY_REQUESTS).build();
        }
        var bankDetails = bankService.verifyBankAccount(executiveId, request.accountNumber(), request.ifscCode(), request.kycFullName());
        return ResponseEntity.ok(bankDetails);
    }

    @PostMapping("/biometric")
    @PreAuthorize("hasRole('DELIVERY')")
    public ResponseEntity<?> verifyBiometric(@Valid @RequestBody BiometricRequest request, Principal principal) {
        UUID executiveId = UUID.fromString(principal.getName());
        io.github.bucket4j.Bucket bucket = rateLimitingService.resolveBucket("verify_biometric:" + executiveId, 3, 3, java.time.Duration.ofHours(1));
        if (!bucket.tryConsume(1)) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.TOO_MANY_REQUESTS).build();
        }
        var response = biometricService.verifySelfie(executiveId, request.selfieUrl());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{executiveId}")
    @PreAuthorize("hasRole('DELIVERY')")
    public ResponseEntity<?> getVerificationSummary(@PathVariable UUID executiveId) {
        if ("dev".equalsIgnoreCase(activeProfile) || "test".equalsIgnoreCase(activeProfile)) {
            return ResponseEntity.ok(new VerificationSummaryResponse(true, true, "MCWG, LMV", true, true, java.time.OffsetDateTime.now().toString()));
        }
        var documents = documentService.getDocumentsForExecutive(executiveId);
        boolean allDocsApproved = java.util.Set.of(DocumentType.DRIVING_LICENSE, DocumentType.RC).stream().allMatch(docType -> documents.stream().anyMatch(d -> d.getDocType() == docType && d.getApiVerificationStatus() == VerificationStatus.APPROVED));
        var bankDetailsOpt = bankService.getBankDetails(executiveId);
        boolean bankApproved = bankDetailsOpt.isPresent() && bankDetailsOpt.get().getPennyDropStatus() == VerificationStatus.APPROVED;
        boolean dlApproved = documents.stream().anyMatch(d -> d.getDocType() == DocumentType.DRIVING_LICENSE && d.getApiVerificationStatus() == VerificationStatus.APPROVED);
        boolean rcApproved = documents.stream().anyMatch(d -> d.getDocType() == DocumentType.RC && d.getApiVerificationStatus() == VerificationStatus.APPROVED);
        // Extract DL vehicle class if available
        String dlVehicleClass = null;
        var dlDocOpt = documents.stream().filter(d -> d.getDocType() == DocumentType.DRIVING_LICENSE && d.getApiVerificationStatus() == VerificationStatus.APPROVED).findFirst();
        if (dlDocOpt.isPresent()) {
            String raw = dlDocOpt.get().getApiRawResponse();
            if (raw != null) {
                try {
                    com.fasterxml.jackson.databind.JsonNode jsonNode = objectMapper.readTree(raw);
                    if (jsonNode.has("vehicleClass") && !jsonNode.get("vehicleClass").isNull()) {
                        dlVehicleClass = jsonNode.get("vehicleClass").asText();
                    }
                } catch (Exception e) {
                    log.warn("Failed to parse JSON for DL response, falling back to regex: {}", e.getMessage());
                    java.util.regex.Matcher m = java.util.regex.Pattern.compile("vehicleClass=([^,]+)").matcher(raw);
                    if (m.find()) {
                        dlVehicleClass = m.group(1).trim();
                    }
                }
            }
        }
        java.time.OffsetDateTime lastBiometric = biometricService.getLastSuccessfulBiometricTime(executiveId);
        String lastBiometricStr = lastBiometric != null ? lastBiometric.toString() : null;
        return ResponseEntity.ok(new VerificationSummaryResponse(allDocsApproved, bankApproved, dlVehicleClass, dlApproved, rcApproved, lastBiometricStr));
    }


    public record VerificationSummaryResponse(boolean allDocsApproved, boolean bankApproved, String dlVehicleClass, boolean dlApproved, boolean rcApproved, String lastBiometricVerificationAt) {
    }

    @java.lang.SuppressWarnings("all")
    public VerificationController(final DrivingLicenseVerificationService dlService, final VehicleVerificationService rcService, final FinancialVerificationService bankService, final BiometricVerificationService biometricService, final ExecutiveDocumentService documentService, final com.fooddelivery.common.service.CloudflareR2Service storageService, final com.fasterxml.jackson.databind.ObjectMapper objectMapper, final com.fooddelivery.common.service.RateLimitingService rateLimitingService) {
        this.dlService = dlService;
        this.rcService = rcService;
        this.bankService = bankService;
        this.biometricService = biometricService;
        this.documentService = documentService;
        this.storageService = storageService;
        this.objectMapper = objectMapper;
        this.rateLimitingService = rateLimitingService;
    }
}
