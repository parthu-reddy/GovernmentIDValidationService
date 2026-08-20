package com.fooddelivery.governmentid.service;

import com.fooddelivery.governmentid.client.RestaurantServiceClient;
import com.fooddelivery.governmentid.entity.BrandBankDetails;
import com.fooddelivery.governmentid.entity.BrandDocument;
import com.fooddelivery.governmentid.entity.BrandVerificationAuditLog;
import com.fooddelivery.governmentid.entity.DocumentType;
import com.fooddelivery.common.enums.VerificationStatus;
import com.fooddelivery.common.enums.VerificationType;
import com.fooddelivery.governmentid.repository.BrandBankDetailsRepository;
import com.fooddelivery.governmentid.repository.BrandDocumentRepository;
import com.fooddelivery.governmentid.event.VerificationAuditEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
@lombok.extern.slf4j.Slf4j
public class BrandVerificationService {
    @java.lang.SuppressWarnings("all")

    private final BrandDocumentRepository documentRepository;
    private final BrandBankDetailsRepository bankDetailsRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final NameMatchingService nameMatchingService;
    private final RestaurantServiceClient restaurantServiceClient;
    private final RedisTemplate<String, String> redisTemplate;
    @Value("${spring.profiles.active:dev}")
    private String activeProfile;
    private static final double SIMILARITY_THRESHOLD = 0.85;

    @Transactional
    @CircuitBreaker(name = "kycProvider", fallbackMethod = "verifyGstinFallback")
    public void verifyGstin(UUID brandId, String gstin, String brandName) {
        log.info("Verifying GSTIN for brand {}", brandId);
        boolean isSuccess;
        String legalName;
        if ("dev".equalsIgnoreCase(activeProfile) || "test".equalsIgnoreCase(activeProfile)) {
            log.info("Dev/Test profile: Bypassing GSTIN verification for brand {}", brandId);
            isSuccess = true;
            legalName = brandName + " PVT LTD";
        } else {
            isSuccess = !gstin.startsWith("INVALID");
            legalName = isSuccess ? brandName + " PVT LTD" : null;
        }
        VerificationStatus status = isSuccess ? VerificationStatus.APPROVED : VerificationStatus.REJECTED;
        BrandDocument doc = documentRepository.findByBrandIdAndDocType(brandId, DocumentType.GSTIN).orElseGet(() -> BrandDocument.builder().id(UUID.randomUUID()).brandId(brandId).docType(DocumentType.GSTIN).build());
        doc.setDocumentNumber(gstin);
        doc.setApiVerificationStatus(status);
        doc.setApiRawResponse(asJson("legalName", legalName));
        doc.setVerifiedAt(OffsetDateTime.now());
        documentRepository.save(doc);
        // Audit log
        BrandVerificationAuditLog audit = BrandVerificationAuditLog.builder().id(UUID.randomUUID()).entityType("BRAND").entityId(brandId).verificationProvider("KARZA_GSTIN").rawRequestPayload(asJson("gstin", gstin)).rawResponsePayload(doc.getApiRawResponse()).status(status.name()).createdAt(LocalDateTime.now()).build();
        eventPublisher.publishEvent(new VerificationAuditEvent(this, audit));
        // Callback to Restaurant Service
        RestaurantServiceClient.VerificationCallbackRequest callback = new RestaurantServiceClient.VerificationCallbackRequest();
        callback.setVerificationType(VerificationType.GSTIN.name());
        callback.setStatus(status.name());
        callback.setLegalEntityName(legalName);
        sendCallbackAfterCommit(brandId, callback, "GSTIN");
    }

    public void verifyGstinFallback(UUID brandId, String gstin, String brandName, Throwable t) {
        log.error("Circuit Breaker fallback for verifyGstin for brand {}: {}", brandId, t.getMessage());
        throw new RuntimeException("KYC Provider unavailable for GSTIN verification. Retrying.", t);
    }

    @Transactional
    @CircuitBreaker(name = "kycProvider", fallbackMethod = "initiatePennyDropFallback")
    public void initiatePennyDrop(UUID brandId, String accountNumber, String ifsc, String brandName) {
        log.info("Initiating Penny Drop for brand {}", brandId);
        BrandBankDetails bankDetails = bankDetailsRepository.findByBrandId(brandId).orElseGet(() -> BrandBankDetails.builder().id(UUID.randomUUID()).brandId(brandId).build());
        bankDetails.setAccountNumber(accountNumber);
        bankDetails.setIfscCode(ifsc);
        bankDetails.setPennyDropStatus(VerificationStatus.PENDING);
        bankDetailsRepository.save(bankDetails);
        // In reality, this would call Signzy/Cashfree which would hit our webhook asynchronously
        // For testing, we won't mock the synchronous response here. 
        // The mock provider will trigger processPennyDropWebhook separately.
        if ("dev".equalsIgnoreCase(activeProfile) || "test".equalsIgnoreCase(activeProfile)) {
            log.info("MOCKING Penny Drop Webhook for Dev Profile");
            processPennyDropWebhook(brandId, brandName, true, brandName);
        }
    }

    public void initiatePennyDropFallback(UUID brandId, String accountNumber, String ifsc, String brandName, Throwable t) {
        log.error("Circuit Breaker fallback for initiatePennyDrop for brand {}: {}", brandId, t.getMessage());
        throw new RuntimeException("KYC Provider unavailable for Penny Drop. Retrying.", t);
    }

    @Transactional
    public void processPennyDropWebhook(UUID brandId, String beneficiaryName, boolean isSuccess, String registeredBrandName) {
        log.info("Processing Penny Drop Webhook for brand {}", brandId);
        String redisKey = "webhook:pennydrop:" + brandId;
        Boolean lockAcquired = redisTemplate.opsForValue().setIfAbsent(redisKey, "locked", java.time.Duration.ofHours(24));
        if (Boolean.FALSE.equals(lockAcquired)) {
            log.info("Webhook already processed or processing for brand {}. Redis lock prevents duplicate.", brandId);
            return;
        }
        try {
            BrandBankDetails bankDetails = bankDetailsRepository.findByBrandId(brandId).orElseGet(() -> BrandBankDetails.builder().id(UUID.randomUUID()).brandId(brandId).build());
            if (bankDetails.getPennyDropStatus() == VerificationStatus.APPROVED || bankDetails.getPennyDropStatus() == VerificationStatus.FAILED) {
                log.info("Webhook already processed for brand {} with status {}. Ignoring duplicate.", brandId, bankDetails.getPennyDropStatus());
                return;
            }
            bankDetails.setBankRegisteredName(beneficiaryName);
            VerificationStatus status;
            Double score = null;
            if ("dev".equalsIgnoreCase(activeProfile) || "test".equalsIgnoreCase(activeProfile)) {
                log.info("Dev/Test profile: Bypassing name match in Penny Drop webhook for brand {}", brandId);
                score = 1.0;
                status = VerificationStatus.APPROVED;
                if (beneficiaryName == null) beneficiaryName = registeredBrandName != null ? registeredBrandName : "DEV BENEFICIARY";
                bankDetails.setNameMatchScore(BigDecimal.valueOf(score));
            } else if (isSuccess && beneficiaryName != null) {
                NameMatchingService.MatchResult result = nameMatchingService.evaluateNameMatch(registeredBrandName, beneficiaryName);
                score = result.score();
                bankDetails.setNameMatchScore(BigDecimal.valueOf(score));
                if (score >= SIMILARITY_THRESHOLD) {
                    status = VerificationStatus.APPROVED;
                } else {
                    status = VerificationStatus.REJECTED;
                    log.warn("Brand {} name mismatch. Score: {}", brandId, score);
                }
            } else {
                status = VerificationStatus.FAILED;
            }
            bankDetails.setPennyDropStatus(status);
            bankDetails.setVerifiedAt(OffsetDateTime.now());
            bankDetailsRepository.save(bankDetails);
            // Audit log
            BrandVerificationAuditLog audit = BrandVerificationAuditLog.builder().id(UUID.randomUUID()).entityType("BRAND").entityId(brandId).verificationProvider("PENNY_DROP_WEBHOOK").rawRequestPayload("{\"webhook_received\":true}").rawResponsePayload("{\"beneficiaryName\":\"" + beneficiaryName + "\"}").similarityScore(score).status(status.name()).createdAt(LocalDateTime.now()).build();
            eventPublisher.publishEvent(new VerificationAuditEvent(this, audit));
            // Callback
            RestaurantServiceClient.VerificationCallbackRequest callback = new RestaurantServiceClient.VerificationCallbackRequest();
            callback.setVerificationType(VerificationType.PENNY_DROP.name());
            callback.setStatus(status.name());
            callback.setBankBeneficiaryName(beneficiaryName);
            callback.setMatchScore(score);
            sendCallbackAfterCommit(brandId, callback, "Penny Drop");
        } catch (Exception e) {
            redisTemplate.delete(redisKey);
            log.error("Failed to process penny drop webhook, releasing Redis lock so it can be retried", e);
            throw e;
        }
    }

    private void sendCallbackAfterCommit(UUID brandId, RestaurantServiceClient.VerificationCallbackRequest callback, String typeName) {
        Runnable task = () -> {
            try {
                restaurantServiceClient.updateVerificationStatus(brandId, callback);
            } catch (Exception e) {
                log.error("Failed to send {} verification callback to restaurant service", typeName, e);
            }
        };
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    task.run();
                }
            });
        } else {
            task.run();
        }
    }

    @java.lang.SuppressWarnings("all")
    public BrandVerificationService(final BrandDocumentRepository documentRepository, final BrandBankDetailsRepository bankDetailsRepository, final ApplicationEventPublisher eventPublisher, final NameMatchingService nameMatchingService, final RestaurantServiceClient restaurantServiceClient, final RedisTemplate<String, String> redisTemplate) {
        this.documentRepository = documentRepository;
        this.bankDetailsRepository = bankDetailsRepository;
        this.eventPublisher = eventPublisher;
        this.nameMatchingService = nameMatchingService;
        this.restaurantServiceClient = restaurantServiceClient;
        this.redisTemplate = redisTemplate;
    }
    private static final com.fasterxml.jackson.databind.ObjectMapper JSON_MAPPER =
            new com.fasterxml.jackson.databind.ObjectMapper();

    /**
     * Builds a single-field JSON object with proper escaping.
     *
     * <p>These payloads previously used string concatenation on values that are not controlled by
     * this service -- {@code gstin} is user-supplied and {@code legalName} comes from the external
     * verification provider -- so a quote or backslash in either produced malformed JSON in the
     * stored document and audit log.
     */
    private static String asJson(String field, String value) {
        try {
            return JSON_MAPPER.writeValueAsString(java.util.Collections.singletonMap(field, value));
        } catch (Exception e) {
            return "{}";
        }
    }
}
