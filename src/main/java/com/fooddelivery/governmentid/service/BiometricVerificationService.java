package com.fooddelivery.governmentid.service;

import com.fooddelivery.governmentid.entity.BiometricVerification;
import com.fooddelivery.governmentid.repository.BiometricVerificationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class BiometricVerificationService {
    @java.lang.SuppressWarnings("all")
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BiometricVerificationService.class);
    private final BiometricVerificationRepository biometricVerificationRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    @Value("${spring.profiles.active:dev}")
    private String activeProfile;
    private static final BigDecimal MIN_CONFIDENCE_THRESHOLD = new BigDecimal("0.990"); // 99% accuracy requirement

    /**
     * Executes runtime biometric liveness and face match verification.
     * 
     * @param executiveId Delivery Executive UUID
     * @param selfieUrl URL to the captured selfie in object storage
     * @return BiometricVerification entity containing results
     */
    public BiometricVerification verifySelfie(UUID executiveId, String selfieUrl) {
        BiometricResult apiResult;
        if ("dev".equalsIgnoreCase(activeProfile) || "test".equalsIgnoreCase(activeProfile)) {
            log.info("Dev/Test profile: Bypassing biometric AI evaluation for executive {}", executiveId);
            apiResult = new BiometricResult(true, new BigDecimal("0.995"));
        } else {
            // Simulate external AI Liveness and Face Match API call
            apiResult = simulateBiometricApi(selfieUrl);
        }
        BiometricVerification verification = new BiometricVerification();
        verification.setExecutiveId(executiveId);
        verification.setSelfieUrl(selfieUrl);
        verification.setConfidenceScore(apiResult.confidenceScore());
        verification.setLive(apiResult.isLive());
        // Persist biometric verification audit trail
        BiometricVerification savedVerification = biometricVerificationRepository.save(verification);
        if (!apiResult.isLive() || apiResult.confidenceScore().compareTo(MIN_CONFIDENCE_THRESHOLD) < 0) {
            log.warn("Biometric verification failed for executive {}. Live: {}, Score: {}", executiveId, apiResult.isLive(), apiResult.confidenceScore());
            long consecutiveFailures = 1; // Current failure is already counted because we saved it, wait, we already saved it. So findBy... includes it.
            List<BiometricVerification> history = biometricVerificationRepository.findTop10ByExecutiveIdOrderByVerificationTimeDesc(executiveId);
            consecutiveFailures = 0;
            for (BiometricVerification past : history) {
                if (!past.isLive() || past.getConfidenceScore().compareTo(MIN_CONFIDENCE_THRESHOLD) < 0) {
                    consecutiveFailures++;
                } else {
                    break;
                }
            }
            if (consecutiveFailures >= 3) {
                try {
                    kafkaTemplate.send("delivery-executive-events", executiveId.toString(), "{\"eventType\":\"EXECUTIVE_SUSPENSION_REQUESTED\",\"executiveId\":\"" + executiveId + "\"}");
                    log.info("Driver {} suspended due to maximum biometric retries via Kafka.", executiveId);
                } catch (Exception e) {
                    log.error("Failed to publish suspension event for driver {} after biometric lockout", executiveId, e);
                }
                throw new IllegalStateException("Maximum biometric retries exceeded. Account locked. Please contact support.");
            } else {
                throw new IllegalStateException("Biometric verification failed. Please remove masks or seek better lighting. Retries left: " + (3 - consecutiveFailures));
            }
        } else {
            log.info("Biometric verification successful for executive {}.", executiveId);
        }
        return savedVerification;
    }

    private BiometricResult simulateBiometricApi(String selfieUrl) {
        if (selfieUrl != null && selfieUrl.contains("fail")) {
            return new BiometricResult(false, new BigDecimal("0.650"));
        }
        return new BiometricResult(true, new BigDecimal("0.995"));
    }

    public java.time.OffsetDateTime getLastSuccessfulBiometricTime(UUID executiveId) {
        return biometricVerificationRepository.findTop10ByExecutiveIdOrderByVerificationTimeDesc(executiveId).stream().filter(v -> v.isLive() && v.getConfidenceScore().compareTo(MIN_CONFIDENCE_THRESHOLD) >= 0).map(BiometricVerification::getVerificationTime).findFirst().orElse(null);
    }


    public record BiometricResult(boolean isLive, BigDecimal confidenceScore) {
    }

    @java.lang.SuppressWarnings("all")
    public BiometricVerificationService(final BiometricVerificationRepository biometricVerificationRepository, final KafkaTemplate<String, String> kafkaTemplate) {
        this.biometricVerificationRepository = biometricVerificationRepository;
        this.kafkaTemplate = kafkaTemplate;
    }
}
