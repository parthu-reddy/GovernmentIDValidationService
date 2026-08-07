package com.fooddelivery.governmentid.service;

import com.fooddelivery.common.enums.VerificationStatus;
import com.fooddelivery.governmentid.util.JaroWinklerMatcher;
import org.springframework.stereotype.Service;

@Service
public class NameMatchingService {
    @java.lang.SuppressWarnings("all")
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(NameMatchingService.class);
    // Threshold configurations balancing security and operational scale
    private static final double AUTO_APPROVE_THRESHOLD = 0.85;
    private static final double MANUAL_REVIEW_THRESHOLD = 0.7;

    /**
     * Executes fuzzy comparison of the KYC identity name against the Bank Account beneficiary name.
     * 
     * @param kycName Name extracted via OCR from PAN or Aadhaar
     * @param bankName Name returned directly by the IMPS Penny Drop API
     * @return MatchResult DTO containing the absolute score and system status
     */
    public MatchResult evaluateNameMatch(String kycName, String bankName) {
        if (kycName == null || bankName == null) {
            throw new IllegalArgumentException("Comparison names cannot be null");
        }
        // Data Normalization: Force lowercase and strip all non-alphabetic special characters
        String normalizedKyc = kycName.trim().toLowerCase().replaceAll("[^a-z\\s]", "");
        String normalizedBank = bankName.trim().toLowerCase().replaceAll("[^a-z\\s]", "");
        // Apply custom Jaro-Winkler calculation yielding a double between 0.0 and 1.0
        Double score = JaroWinklerMatcher.computeSimilarity(normalizedKyc, normalizedBank);
        log.info("Jaro-Winkler evaluation for KYC [{}] and Bank [{}]: {}", maskName(normalizedKyc), maskName(normalizedBank), score);
        VerificationStatus status;
        if (score >= AUTO_APPROVE_THRESHOLD) {
            status = VerificationStatus.APPROVED;
        } else if (score >= MANUAL_REVIEW_THRESHOLD) {
            status = VerificationStatus.MANUAL_REVIEW;
        } else {
            status = VerificationStatus.REJECTED;
        }
        return new MatchResult(score, status);
    }

    private String maskName(String name) {
        if (name == null || name.length() <= 2) return "***";
        return name.charAt(0) + "***" + name.charAt(name.length() - 1);
    }

    // Modern Java Record DTO for immutable data transfer
    public record MatchResult(Double score, VerificationStatus status) {
    }
}
