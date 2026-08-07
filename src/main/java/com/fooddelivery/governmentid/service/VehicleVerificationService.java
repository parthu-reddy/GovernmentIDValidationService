package com.fooddelivery.governmentid.service;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import com.fooddelivery.governmentid.exception.ExternalVerificationException;

@Service
public class VehicleVerificationService {
    @java.lang.SuppressWarnings("all")
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(VehicleVerificationService.class);
    private final RestClient.Builder restClientBuilder;
    @Value("${spring.profiles.active:dev}")
    private String activeProfile;
    @Value("${verification.vahan.api.url:https://api.mock-vahan.gov.in/rc/verify}")
    private String vahanApiUrl;
    @Value("${verification.vahan.api.key:mock-api-key}")
    private String apiKey;
    @Value("${verification.vahan.api.timeout-seconds:10}")
    private int timeoutSeconds;
    /**
     * Built once during initialization to reuse connection pool and configuration.
     */
    private RestClient vahanClient;

    @PostConstruct
    void init() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(timeoutSeconds * 1000);
        requestFactory.setReadTimeout(timeoutSeconds * 1000);
        vahanClient = restClientBuilder.baseUrl(vahanApiUrl).defaultHeader("Authorization", "Bearer " + apiKey).requestFactory(requestFactory).build();
    }

    private static final java.util.regex.Pattern RC_NUMBER_PATTERN = java.util.regex.Pattern.compile("^[A-Za-z0-9\\s\\-]{6,15}$");

    /**
     * Executes real-time validation of Vehicle Registration Certificate against Vahan Database.
     * 
     * @param registrationNumber Vehicle License Plate Number (e.g., MH01AB1234)
     * @return RCVerificationResponse DTO encapsulating the registry data
     */
    public RCVerificationResponse verifyVehicleRC(String registrationNumber) {
        if ("dev".equalsIgnoreCase(activeProfile) || "test".equalsIgnoreCase(activeProfile)) {
            log.info("MOCKING RC Verification for Dev/Test Profile. RegNo: {}", registrationNumber);
            return new RCVerificationResponse(true, "MOCK DEV OWNER", "FIT", "2030-12-31", "Mocked success for dev");
        }
        if (!RC_NUMBER_PATTERN.matcher(registrationNumber).matches()) {
            throw new IllegalArgumentException("Invalid Vehicle Registration Number format.");
        }
        String requestBody = String.format("{\"reg_no\": \"%s\"}", registrationNumber);
        try {
            JsonNode response = vahanClient.post().uri("").contentType(MediaType.APPLICATION_JSON).body(requestBody).retrieve().body(JsonNode.class);
            return parseVahanResponse(response);
        } catch (Exception e) {
            log.error("Network or Authentication failure communicating with Vahan API for RC: {}", maskRegistration(registrationNumber), e);
            throw new ExternalVerificationException("RC Verification API unavailable. Initiate fallback queue.", e);
        }
    }

    private RCVerificationResponse parseVahanResponse(JsonNode responseNode) {
        if (responseNode == null) {
            return new RCVerificationResponse(false, null, null, null, "Empty response from Vahan API");
        }
        if (responseNode.has("error") && "true".equals(responseNode.get("error").asText())) {
            log.warn("Vahan API rejected payload: {}", responseNode.get("message").asText());
            return new RCVerificationResponse(false, null, null, null, responseNode.get("message").asText());
        }
        JsonNode data = responseNode.get("result");
        if (data != null) {
            String ownerName = data.has("owner_name") ? data.get("owner_name").asText() : null;
            String fitnessStatus = data.has("fitness_status") ? data.get("fitness_status").asText() : null;
            String insuranceExpiry = data.has("insurance_expiry") ? data.get("insurance_expiry").asText() : null;
            return new RCVerificationResponse(true, ownerName, fitnessStatus, insuranceExpiry, "Success");
        }
        return new RCVerificationResponse(false, null, null, null, "Invalid response payload structure");
    }

    /**
     * Masks a registration number for log safety: shows first 4 chars only.
     */
    private String maskRegistration(String regNumber) {
        if (regNumber == null || regNumber.length() <= 4) return "****";
        return regNumber.substring(0, 4) + "****";
    }


    public record RCVerificationResponse(boolean isValid, String ownerName, String fitnessStatus, String insuranceExpiry, String message) {
    }

    @java.lang.SuppressWarnings("all")
    public VehicleVerificationService(final RestClient.Builder restClientBuilder) {
        this.restClientBuilder = restClientBuilder;
    }
}
