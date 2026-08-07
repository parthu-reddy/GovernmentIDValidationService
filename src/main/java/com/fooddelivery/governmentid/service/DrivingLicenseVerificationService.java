package com.fooddelivery.governmentid.service;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.http.MediaType;
import java.time.Duration;
import java.util.regex.Pattern;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import com.fooddelivery.governmentid.exception.ExternalVerificationException;

@Service
public class DrivingLicenseVerificationService {
    @java.lang.SuppressWarnings("all")
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DrivingLicenseVerificationService.class);
    private final RestClient.Builder restClientBuilder;
    @Value("${spring.profiles.active:dev}")
    private String activeProfile;
    @Value("${verification.sarathi.api.url:https://api.mock-sarathi.gov.in/dl/verify}")
    private String sarathiApiUrl;
    @Value("${verification.sarathi.api.key:mock-api-key}")
    private String apiKey;
    @Value("${verification.sarathi.api.timeout-seconds:10}")
    private int timeoutSeconds;
    private static final Pattern DL_NUMBER_PATTERN = Pattern.compile("^[A-Za-z0-9\\s\\-]{10,20}$");
    private static final Pattern DOB_PATTERN = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
    /**
     * Built once during initialization to reuse connection pool and configuration.
     */
    private RestClient sarathiClient;

    @PostConstruct
    void init() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(timeoutSeconds * 1000);
        requestFactory.setReadTimeout(timeoutSeconds * 1000);
        sarathiClient = restClientBuilder.baseUrl(sarathiApiUrl).defaultHeader("Authorization", "Bearer " + apiKey).requestFactory(requestFactory).build();
    }

    /**
     * Executes real-time validation of the Driving License against the Sarathi Database.
     * 
     * @param dlNumber 16-digit alphanumeric DL Number (e.g., RJ14 20110012345)
     * @param dateOfBirth Format constraint: yyyy-mm-dd
     * @return DLVerificationResponse DTO encapsulating the registry data
     */
    public DLVerificationResponse verifyDrivingLicense(String dlNumber, String dateOfBirth) {
        if ("dev".equalsIgnoreCase(activeProfile) || "test".equalsIgnoreCase(activeProfile)) {
            log.info("MOCKING DL Verification for Dev/Test Profile. DL: {}, DOB: {}", dlNumber, dateOfBirth);
            return new DLVerificationResponse(true, "MOCK DEV USER", "LMV", "2030-12-31", "Mocked success for dev");
        }
        if (!DL_NUMBER_PATTERN.matcher(dlNumber).matches()) {
            throw new IllegalArgumentException("Invalid Driving License format. Expected 15 alphanumeric characters (e.g., RJ14 20110012345)");
        }
        if (!DOB_PATTERN.matcher(dateOfBirth).matches()) {
            throw new IllegalArgumentException("Invalid date of birth format. Expected yyyy-MM-dd");
        }
        // Construct the strict JSON Request Body required by the provider
        String requestBody = String.format("{\"dlnumber\": \"%s\", \"dob\": \"%s\"}", dlNumber, dateOfBirth);
        try {
            // Execute synchronous POST request to external Sarathi integration gateway
            JsonNode response = sarathiClient.post().uri("").contentType(MediaType.APPLICATION_JSON).body(requestBody).retrieve().body(JsonNode.class);
            return parseSarathiResponse(response);
        } catch (Exception e) {
            log.error("Network or Authentication failure communicating with Sarathi API for DL: {}", maskDlNumber(dlNumber), e);
            throw new ExternalVerificationException("DL Verification API unavailable. Initiate fallback queue.", e);
        }
    }

    private DLVerificationResponse parseSarathiResponse(JsonNode responseNode) {
        if (responseNode == null) {
            return new DLVerificationResponse(false, null, null, null, "Empty response from Sarathi API");
        }
        // Evaluate the JSON structure based on provider specifications for HTTP 400 bad formats
        if (responseNode.has("error") && "true".equals(responseNode.get("error").asText())) {
            log.warn("Sarathi API rejected payload: {}", responseNode.get("message").asText());
            return new DLVerificationResponse(false, null, null, null, responseNode.get("message").asText());
        }
        // Extract relevant fields assuming successful HTTP 200 payload
        JsonNode responseArray = responseNode.get("response");
        if (responseArray != null && responseArray.isArray() && responseArray.size() > 0) {
            JsonNode data = responseArray.get(0).get("response");
            if (data != null && data.has("licOj")) {
                String holderName = data.get("licOj").get("name").asText();
                String vehicleClass = data.get("licOj").get("cov").asText(); // E.g., MCWG
                String expiryDate = data.has("doe") ? data.get("doe").asText() : null;
                return new DLVerificationResponse(true, holderName, vehicleClass, expiryDate, "Success");
            }
        }
        return new DLVerificationResponse(false, null, null, null, "Invalid response payload structure");
    }

    /**
     * Masks a DL number for log safety: shows first 4 and last 4 characters only.
     */
    private String maskDlNumber(String dlNumber) {
        if (dlNumber == null || dlNumber.length() <= 8) return "****";
        return dlNumber.substring(0, 4) + "****" + dlNumber.substring(dlNumber.length() - 4);
    }


    public record DLVerificationResponse(boolean isValid, String name, String vehicleClass, String expiryDate, String message) {
    }

    @java.lang.SuppressWarnings("all")
    public DrivingLicenseVerificationService(final RestClient.Builder restClientBuilder) {
        this.restClientBuilder = restClientBuilder;
    }
}
