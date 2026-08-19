package com.fooddelivery.govvalidation;

import com.fooddelivery.governmentid.controller.VerificationController;
import com.fooddelivery.governmentid.service.*;
import com.fooddelivery.common.service.CloudflareR2Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public abstract class ContractTestBase {

    @BeforeEach
    public void setup() {
        DrivingLicenseVerificationService dlService = Mockito.mock(DrivingLicenseVerificationService.class);
        VehicleVerificationService rcService = Mockito.mock(VehicleVerificationService.class);
        FinancialVerificationService bankService = Mockito.mock(FinancialVerificationService.class);
        BiometricVerificationService biometricService = Mockito.mock(BiometricVerificationService.class);
        ExecutiveDocumentService documentService = Mockito.mock(ExecutiveDocumentService.class);
        CloudflareR2Service storageService = Mockito.mock(CloudflareR2Service.class);
        ObjectMapper objectMapper = new ObjectMapper();

        // Mock setups
        when(dlService.verifyDrivingLicense(any(), any())).thenReturn(new com.fooddelivery.governmentid.service.DrivingLicenseVerificationService.DLVerificationResponse(true, "MockName", "LMV", "12-12-2030", "Success"));

        com.fooddelivery.governmentid.entity.ExecutiveDocument dlDoc = Mockito.mock(com.fooddelivery.governmentid.entity.ExecutiveDocument.class);
        when(dlDoc.getDocType()).thenReturn(com.fooddelivery.governmentid.entity.DocumentType.DRIVING_LICENSE);
        when(dlDoc.getApiVerificationStatus()).thenReturn(com.fooddelivery.common.enums.VerificationStatus.APPROVED);
        when(dlDoc.getApiRawResponse()).thenReturn("{\"vehicleClass\":\"LMV\"}");

        com.fooddelivery.governmentid.entity.ExecutiveDocument rcDoc = Mockito.mock(com.fooddelivery.governmentid.entity.ExecutiveDocument.class);
        when(rcDoc.getDocType()).thenReturn(com.fooddelivery.governmentid.entity.DocumentType.RC);
        when(rcDoc.getApiVerificationStatus()).thenReturn(com.fooddelivery.common.enums.VerificationStatus.APPROVED);

        when(documentService.getDocumentsForExecutive(any())).thenReturn(java.util.List.of(dlDoc, rcDoc));

        com.fooddelivery.governmentid.entity.ExecutiveBankDetails bankDetails = Mockito.mock(com.fooddelivery.governmentid.entity.ExecutiveBankDetails.class);
        when(bankDetails.getPennyDropStatus()).thenReturn(com.fooddelivery.common.enums.VerificationStatus.APPROVED);
        when(bankService.getBankDetails(any())).thenReturn(java.util.Optional.of(bankDetails));

        when(biometricService.getLastSuccessfulBiometricTime(any())).thenReturn(java.time.OffsetDateTime.parse("2023-10-01T12:00:00Z"));

        VerificationController controller = new VerificationController(dlService, rcService, bankService, biometricService, documentService, storageService, objectMapper);

        // Brand KYC endpoints consumed by RestaurantApplication (verifyGstin, verifyBrandBankAccount).
        BrandVerificationService brandVerificationService = Mockito.mock(BrandVerificationService.class);
        com.fooddelivery.governmentid.controller.BrandVerificationController brandController =
                new com.fooddelivery.governmentid.controller.BrandVerificationController(brandVerificationService);

        RestAssuredMockMvc.standaloneSetup(controller, brandController);
    }
}
