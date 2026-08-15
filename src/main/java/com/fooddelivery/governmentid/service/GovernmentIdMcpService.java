package com.fooddelivery.governmentid.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fooddelivery.governmentid.controller.BrandVerificationController;
import com.fooddelivery.governmentid.controller.VerificationController;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;
import java.security.Principal;
import java.util.UUID;

@Service
@lombok.extern.slf4j.Slf4j
public class GovernmentIdMcpService {
    @java.lang.SuppressWarnings("all")

    private final VerificationController verificationController;
    private final BrandVerificationController brandVerificationController;
    private final ObjectMapper objectMapper;

    public GovernmentIdMcpService(VerificationController verificationController, BrandVerificationController brandVerificationController, ObjectMapper objectMapper) {
        this.verificationController = verificationController;
        this.brandVerificationController = brandVerificationController;
        this.objectMapper = objectMapper;
    }

    private Principal createMockPrincipal(String executiveId) {
        return () -> executiveId;
    }

    // VerificationController
    @Tool(description = "Get presigned upload url. Provide executiveId, docType (e.g. DRIVING_LICENSE, RC), and contentType (e.g. image/jpeg).")
    public String getPresignedUploadUrl(String executiveId, String docType, String contentType) {
        try {
            return objectMapper.writeValueAsString(verificationController.getPresignedUploadUrl(com.fooddelivery.governmentid.entity.DocumentType.valueOf(docType), contentType, createMockPrincipal(executiveId)).getBody());
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @Tool(description = "Get presigned download url. Provide executiveId and objectKey.")
    public String getPresignedDownloadUrl(String executiveId, String objectKey) {
        try {
            return objectMapper.writeValueAsString(verificationController.getPresignedDownloadUrl(objectKey, createMockPrincipal(executiveId)).getBody());
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @Tool(description = "Verify Driving License. Provide executiveId and JSON string of DLRequest (dlNumber, documentUrl, dob).")
    public String verifyDrivingLicense(String executiveId, String requestJson) {
        try {
            VerificationController.DLRequest req = objectMapper.readValue(requestJson, VerificationController.DLRequest.class);
            return objectMapper.writeValueAsString(verificationController.verifyDrivingLicense(req, createMockPrincipal(executiveId)).getBody());
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @Tool(description = "Verify Vehicle RC. Provide executiveId and JSON string of RCRequest (registrationNumber, documentUrl).")
    public String verifyVehicleRC(String executiveId, String requestJson) {
        try {
            VerificationController.RCRequest req = objectMapper.readValue(requestJson, VerificationController.RCRequest.class);
            return objectMapper.writeValueAsString(verificationController.verifyVehicleRC(req, createMockPrincipal(executiveId)).getBody());
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @Tool(description = "Verify Bank Account for Executive. Provide executiveId and JSON string of BankRequest (accountNumber, ifscCode, kycFullName).")
    public String verifyExecutiveBankAccount(String executiveId, String requestJson) {
        try {
            VerificationController.BankRequest req = objectMapper.readValue(requestJson, VerificationController.BankRequest.class);
            return objectMapper.writeValueAsString(verificationController.verifyBankAccount(req, createMockPrincipal(executiveId)).getBody());
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @Tool(description = "Verify Biometric Selfie. Provide executiveId and JSON string of BiometricRequest (selfieUrl).")
    public String verifyBiometric(String executiveId, String requestJson) {
        try {
            VerificationController.BiometricRequest req = objectMapper.readValue(requestJson, VerificationController.BiometricRequest.class);
            return objectMapper.writeValueAsString(verificationController.verifyBiometric(req, createMockPrincipal(executiveId)).getBody());
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @Tool(description = "Get verification summary. Provide executiveId.")
    public String getVerificationSummary(String executiveId) {
        try {
            return objectMapper.writeValueAsString(verificationController.getVerificationSummary(UUID.fromString(executiveId)).getBody());
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    // BrandVerificationController
    @Tool(description = "Verify Brand GSTIN. Provide JSON string of GstinRequest (brandId, gstin, brandName).")
    public String verifyBrandGstin(String requestJson) {
        try {
            BrandVerificationController.GstinRequest req = objectMapper.readValue(requestJson, BrandVerificationController.GstinRequest.class);
            return objectMapper.writeValueAsString(brandVerificationController.verifyGstin(req).getBody());
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @Tool(description = "Verify Brand Bank Account. Provide JSON string of BankAccountRequest (brandId, accountNumber, ifscCode, brandName).")
    public String verifyBrandBankAccount(String requestJson) {
        try {
            BrandVerificationController.BankAccountRequest req = objectMapper.readValue(requestJson, BrandVerificationController.BankAccountRequest.class);
            return objectMapper.writeValueAsString(brandVerificationController.verifyBankAccount(req).getBody());
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @Tool(description = "Handle Penny Drop Webhook. Provide JSON string of PennyDropWebhook (brandId, beneficiaryName, status, registeredBrandName).")
    public String handlePennyDropWebhook(String requestJson) {
        try {
            BrandVerificationController.PennyDropWebhook req = objectMapper.readValue(requestJson, BrandVerificationController.PennyDropWebhook.class);
            return objectMapper.writeValueAsString(brandVerificationController.handlePennyDropWebhook(req).getBody());
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
