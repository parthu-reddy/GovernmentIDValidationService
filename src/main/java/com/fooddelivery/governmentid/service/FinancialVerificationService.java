package com.fooddelivery.governmentid.service;

import com.fooddelivery.governmentid.entity.ExecutiveBankDetails;
import com.fooddelivery.common.enums.VerificationStatus;
import com.fooddelivery.governmentid.repository.ExecutiveBankDetailsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FinancialVerificationService {

    private final ExecutiveBankDetailsRepository bankDetailsRepository;
    private final NameMatchingService nameMatchingService;

    /**
     * Executes IMPS Penny Drop verification and matches the beneficiary name using Jaro-Winkler.
     * 
     * @param executiveId Delivery Executive UUID
     * @param accountNumber Bank Account Number
     * @param ifscCode Bank IFSC Code
     * @return ExecutiveBankDetails updated entity
     */
    @Transactional
    public ExecutiveBankDetails verifyBankAccount(UUID executiveId, String accountNumber, String ifscCode, String kycFullName) {

        // Simulate IMPS Penny Drop API Call returning the registered bank name
        String simulatedBankBeneficiaryName = simulateImpsPennyDrop(accountNumber, ifscCode, kycFullName);

        // Perform fuzzy name match against KYC Name
        NameMatchingService.MatchResult matchResult = nameMatchingService.evaluateNameMatch(kycFullName, simulatedBankBeneficiaryName);

        ExecutiveBankDetails bankDetails = bankDetailsRepository.findByExecutiveId(executiveId)
                .orElseGet(ExecutiveBankDetails::new);
        
        bankDetails.setExecutiveId(executiveId);
        bankDetails.setAccountNumber(accountNumber);
        bankDetails.setIfscCode(ifscCode);
        bankDetails.setBankRegisteredName(simulatedBankBeneficiaryName);
        bankDetails.setNameMatchScore(BigDecimal.valueOf(matchResult.score()));
        bankDetails.setPennyDropStatus(matchResult.status());
        bankDetails.setVerifiedAt(OffsetDateTime.now());

        log.info("Penny drop verification completed for executive {}. Status: {}, Score: {}", 
                 executiveId, matchResult.status(), matchResult.score());

        return bankDetailsRepository.save(bankDetails);
    }

    private String maskAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.length() <= 4) return "****";
        return "****" + accountNumber.substring(accountNumber.length() - 4);
    }

    private String simulateImpsPennyDrop(String accountNumber, String ifscCode, String kycFullName) {
        // In real environment, call banking gateway (e.g., Razorpay, Cashfree, Signzy)
        // Here we simulate returning a slightly transliterated or exact name for testing
        if (accountNumber.startsWith("999")) {
            return "Unauthorized Person"; // Simulate complete mismatch
        }
        return kycFullName; // Return exact name to pass fuzzy matching
    }

    public java.util.Optional<ExecutiveBankDetails> getBankDetails(UUID executiveId) {
        return bankDetailsRepository.findByExecutiveId(executiveId);
    }
}
