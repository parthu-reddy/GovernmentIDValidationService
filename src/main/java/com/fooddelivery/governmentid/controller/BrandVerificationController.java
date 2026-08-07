package com.fooddelivery.governmentid.controller;

import com.fooddelivery.governmentid.service.BrandVerificationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/verification/brands")
public class BrandVerificationController {
    @java.lang.SuppressWarnings("all")
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BrandVerificationController.class);
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
        brandVerificationService.processPennyDropWebhook(request.getBrandId(), request.getBeneficiaryName(), "SUCCESS".equalsIgnoreCase(request.getStatus()), request.getRegisteredBrandName());
        return ResponseEntity.ok().build();
    }


    public static class GstinRequest {
        private UUID brandId;
        @NotBlank
        private String gstin;
        @NotBlank
        private String brandName;

        @java.lang.SuppressWarnings("all")
        public GstinRequest() {
        }

        @java.lang.SuppressWarnings("all")
        public UUID getBrandId() {
            return this.brandId;
        }

        @java.lang.SuppressWarnings("all")
        public String getGstin() {
            return this.gstin;
        }

        @java.lang.SuppressWarnings("all")
        public String getBrandName() {
            return this.brandName;
        }

        @java.lang.SuppressWarnings("all")
        public void setBrandId(final UUID brandId) {
            this.brandId = brandId;
        }

        @java.lang.SuppressWarnings("all")
        public void setGstin(final String gstin) {
            this.gstin = gstin;
        }

        @java.lang.SuppressWarnings("all")
        public void setBrandName(final String brandName) {
            this.brandName = brandName;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        public boolean equals(final java.lang.Object o) {
            if (o == this) return true;
            if (!(o instanceof BrandVerificationController.GstinRequest)) return false;
            final BrandVerificationController.GstinRequest other = (BrandVerificationController.GstinRequest) o;
            if (!other.canEqual((java.lang.Object) this)) return false;
            final java.lang.Object this$brandId = this.getBrandId();
            final java.lang.Object other$brandId = other.getBrandId();
            if (this$brandId == null ? other$brandId != null : !this$brandId.equals(other$brandId)) return false;
            final java.lang.Object this$gstin = this.getGstin();
            final java.lang.Object other$gstin = other.getGstin();
            if (this$gstin == null ? other$gstin != null : !this$gstin.equals(other$gstin)) return false;
            final java.lang.Object this$brandName = this.getBrandName();
            final java.lang.Object other$brandName = other.getBrandName();
            if (this$brandName == null ? other$brandName != null : !this$brandName.equals(other$brandName)) return false;
            return true;
        }

        @java.lang.SuppressWarnings("all")
        protected boolean canEqual(final java.lang.Object other) {
            return other instanceof BrandVerificationController.GstinRequest;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            final java.lang.Object $brandId = this.getBrandId();
            result = result * PRIME + ($brandId == null ? 43 : $brandId.hashCode());
            final java.lang.Object $gstin = this.getGstin();
            result = result * PRIME + ($gstin == null ? 43 : $gstin.hashCode());
            final java.lang.Object $brandName = this.getBrandName();
            result = result * PRIME + ($brandName == null ? 43 : $brandName.hashCode());
            return result;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        public java.lang.String toString() {
            return "BrandVerificationController.GstinRequest(brandId=" + this.getBrandId() + ", gstin=" + this.getGstin() + ", brandName=" + this.getBrandName() + ")";
        }
    }


    public static class BankAccountRequest {
        private UUID brandId;
        @NotBlank
        private String accountNumber;
        @NotBlank
        private String ifscCode;
        @NotBlank
        private String brandName;

        @java.lang.SuppressWarnings("all")
        public BankAccountRequest() {
        }

        @java.lang.SuppressWarnings("all")
        public UUID getBrandId() {
            return this.brandId;
        }

        @java.lang.SuppressWarnings("all")
        public String getAccountNumber() {
            return this.accountNumber;
        }

        @java.lang.SuppressWarnings("all")
        public String getIfscCode() {
            return this.ifscCode;
        }

        @java.lang.SuppressWarnings("all")
        public String getBrandName() {
            return this.brandName;
        }

        @java.lang.SuppressWarnings("all")
        public void setBrandId(final UUID brandId) {
            this.brandId = brandId;
        }

        @java.lang.SuppressWarnings("all")
        public void setAccountNumber(final String accountNumber) {
            this.accountNumber = accountNumber;
        }

        @java.lang.SuppressWarnings("all")
        public void setIfscCode(final String ifscCode) {
            this.ifscCode = ifscCode;
        }

        @java.lang.SuppressWarnings("all")
        public void setBrandName(final String brandName) {
            this.brandName = brandName;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        public boolean equals(final java.lang.Object o) {
            if (o == this) return true;
            if (!(o instanceof BrandVerificationController.BankAccountRequest)) return false;
            final BrandVerificationController.BankAccountRequest other = (BrandVerificationController.BankAccountRequest) o;
            if (!other.canEqual((java.lang.Object) this)) return false;
            final java.lang.Object this$brandId = this.getBrandId();
            final java.lang.Object other$brandId = other.getBrandId();
            if (this$brandId == null ? other$brandId != null : !this$brandId.equals(other$brandId)) return false;
            final java.lang.Object this$accountNumber = this.getAccountNumber();
            final java.lang.Object other$accountNumber = other.getAccountNumber();
            if (this$accountNumber == null ? other$accountNumber != null : !this$accountNumber.equals(other$accountNumber)) return false;
            final java.lang.Object this$ifscCode = this.getIfscCode();
            final java.lang.Object other$ifscCode = other.getIfscCode();
            if (this$ifscCode == null ? other$ifscCode != null : !this$ifscCode.equals(other$ifscCode)) return false;
            final java.lang.Object this$brandName = this.getBrandName();
            final java.lang.Object other$brandName = other.getBrandName();
            if (this$brandName == null ? other$brandName != null : !this$brandName.equals(other$brandName)) return false;
            return true;
        }

        @java.lang.SuppressWarnings("all")
        protected boolean canEqual(final java.lang.Object other) {
            return other instanceof BrandVerificationController.BankAccountRequest;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            final java.lang.Object $brandId = this.getBrandId();
            result = result * PRIME + ($brandId == null ? 43 : $brandId.hashCode());
            final java.lang.Object $accountNumber = this.getAccountNumber();
            result = result * PRIME + ($accountNumber == null ? 43 : $accountNumber.hashCode());
            final java.lang.Object $ifscCode = this.getIfscCode();
            result = result * PRIME + ($ifscCode == null ? 43 : $ifscCode.hashCode());
            final java.lang.Object $brandName = this.getBrandName();
            result = result * PRIME + ($brandName == null ? 43 : $brandName.hashCode());
            return result;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        public java.lang.String toString() {
            return "BrandVerificationController.BankAccountRequest(brandId=" + this.getBrandId() + ", accountNumber=" + this.getAccountNumber() + ", ifscCode=" + this.getIfscCode() + ", brandName=" + this.getBrandName() + ")";
        }
    }


    public static class PennyDropWebhook {
        private UUID brandId;
        private String beneficiaryName;
        private String status;
        private String registeredBrandName; // Passed in webhook or fetched if we saved it initially

        @java.lang.SuppressWarnings("all")
        public PennyDropWebhook() {
        }

        @java.lang.SuppressWarnings("all")
        public UUID getBrandId() {
            return this.brandId;
        }

        @java.lang.SuppressWarnings("all")
        public String getBeneficiaryName() {
            return this.beneficiaryName;
        }

        @java.lang.SuppressWarnings("all")
        public String getStatus() {
            return this.status;
        }

        @java.lang.SuppressWarnings("all")
        public String getRegisteredBrandName() {
            return this.registeredBrandName;
        }

        @java.lang.SuppressWarnings("all")
        public void setBrandId(final UUID brandId) {
            this.brandId = brandId;
        }

        @java.lang.SuppressWarnings("all")
        public void setBeneficiaryName(final String beneficiaryName) {
            this.beneficiaryName = beneficiaryName;
        }

        @java.lang.SuppressWarnings("all")
        public void setStatus(final String status) {
            this.status = status;
        }

        @java.lang.SuppressWarnings("all")
        public void setRegisteredBrandName(final String registeredBrandName) {
            this.registeredBrandName = registeredBrandName;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        public boolean equals(final java.lang.Object o) {
            if (o == this) return true;
            if (!(o instanceof BrandVerificationController.PennyDropWebhook)) return false;
            final BrandVerificationController.PennyDropWebhook other = (BrandVerificationController.PennyDropWebhook) o;
            if (!other.canEqual((java.lang.Object) this)) return false;
            final java.lang.Object this$brandId = this.getBrandId();
            final java.lang.Object other$brandId = other.getBrandId();
            if (this$brandId == null ? other$brandId != null : !this$brandId.equals(other$brandId)) return false;
            final java.lang.Object this$beneficiaryName = this.getBeneficiaryName();
            final java.lang.Object other$beneficiaryName = other.getBeneficiaryName();
            if (this$beneficiaryName == null ? other$beneficiaryName != null : !this$beneficiaryName.equals(other$beneficiaryName)) return false;
            final java.lang.Object this$status = this.getStatus();
            final java.lang.Object other$status = other.getStatus();
            if (this$status == null ? other$status != null : !this$status.equals(other$status)) return false;
            final java.lang.Object this$registeredBrandName = this.getRegisteredBrandName();
            final java.lang.Object other$registeredBrandName = other.getRegisteredBrandName();
            if (this$registeredBrandName == null ? other$registeredBrandName != null : !this$registeredBrandName.equals(other$registeredBrandName)) return false;
            return true;
        }

        @java.lang.SuppressWarnings("all")
        protected boolean canEqual(final java.lang.Object other) {
            return other instanceof BrandVerificationController.PennyDropWebhook;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            final java.lang.Object $brandId = this.getBrandId();
            result = result * PRIME + ($brandId == null ? 43 : $brandId.hashCode());
            final java.lang.Object $beneficiaryName = this.getBeneficiaryName();
            result = result * PRIME + ($beneficiaryName == null ? 43 : $beneficiaryName.hashCode());
            final java.lang.Object $status = this.getStatus();
            result = result * PRIME + ($status == null ? 43 : $status.hashCode());
            final java.lang.Object $registeredBrandName = this.getRegisteredBrandName();
            result = result * PRIME + ($registeredBrandName == null ? 43 : $registeredBrandName.hashCode());
            return result;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        public java.lang.String toString() {
            return "BrandVerificationController.PennyDropWebhook(brandId=" + this.getBrandId() + ", beneficiaryName=" + this.getBeneficiaryName() + ", status=" + this.getStatus() + ", registeredBrandName=" + this.getRegisteredBrandName() + ")";
        }
    }

    @java.lang.SuppressWarnings("all")
    public BrandVerificationController(final BrandVerificationService brandVerificationService) {
        this.brandVerificationService = brandVerificationService;
    }
}
