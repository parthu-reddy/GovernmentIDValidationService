package com.fooddelivery.governmentid.entity;

import jakarta.persistence.Column;
import com.fooddelivery.common.enums.VerificationStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "brand_bank_details")
public class BrandBankDetails {
    @Id
    @Column(name = "id")
    private UUID id;
    @Column(name = "brand_id")
    private UUID brandId;
    @Column(name = "account_number")
    private String accountNumber;
    @Column(name = "ifsc_code")
    private String ifscCode;
    @Column(name = "bank_registered_name")
    private String bankRegisteredName;
    @Column(name = "name_match_score")
    private BigDecimal nameMatchScore;
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @jakarta.persistence.Column(name = "penny_drop_status", columnDefinition = "verification_status")
    private VerificationStatus pennyDropStatus;
    @Column(name = "verified_at")
    private OffsetDateTime verifiedAt;


    @java.lang.SuppressWarnings("all")
    public static class BrandBankDetailsBuilder {
        @java.lang.SuppressWarnings("all")
        private UUID id;
        @java.lang.SuppressWarnings("all")
        private UUID brandId;
        @java.lang.SuppressWarnings("all")
        private String accountNumber;
        @java.lang.SuppressWarnings("all")
        private String ifscCode;
        @java.lang.SuppressWarnings("all")
        private String bankRegisteredName;
        @java.lang.SuppressWarnings("all")
        private BigDecimal nameMatchScore;
        @java.lang.SuppressWarnings("all")
        private VerificationStatus pennyDropStatus;
        @java.lang.SuppressWarnings("all")
        private OffsetDateTime verifiedAt;

        @java.lang.SuppressWarnings("all")
        BrandBankDetailsBuilder() {
        }

        /**
         * @return {@code this}.
         */
        @java.lang.SuppressWarnings("all")
        public BrandBankDetails.BrandBankDetailsBuilder id(final UUID id) {
            this.id = id;
            return this;
        }

        /**
         * @return {@code this}.
         */
        @java.lang.SuppressWarnings("all")
        public BrandBankDetails.BrandBankDetailsBuilder brandId(final UUID brandId) {
            this.brandId = brandId;
            return this;
        }

        /**
         * @return {@code this}.
         */
        @java.lang.SuppressWarnings("all")
        public BrandBankDetails.BrandBankDetailsBuilder accountNumber(final String accountNumber) {
            this.accountNumber = accountNumber;
            return this;
        }

        /**
         * @return {@code this}.
         */
        @java.lang.SuppressWarnings("all")
        public BrandBankDetails.BrandBankDetailsBuilder ifscCode(final String ifscCode) {
            this.ifscCode = ifscCode;
            return this;
        }

        /**
         * @return {@code this}.
         */
        @java.lang.SuppressWarnings("all")
        public BrandBankDetails.BrandBankDetailsBuilder bankRegisteredName(final String bankRegisteredName) {
            this.bankRegisteredName = bankRegisteredName;
            return this;
        }

        /**
         * @return {@code this}.
         */
        @java.lang.SuppressWarnings("all")
        public BrandBankDetails.BrandBankDetailsBuilder nameMatchScore(final BigDecimal nameMatchScore) {
            this.nameMatchScore = nameMatchScore;
            return this;
        }

        /**
         * @return {@code this}.
         */
        @java.lang.SuppressWarnings("all")
        public BrandBankDetails.BrandBankDetailsBuilder pennyDropStatus(final VerificationStatus pennyDropStatus) {
            this.pennyDropStatus = pennyDropStatus;
            return this;
        }

        /**
         * @return {@code this}.
         */
        @java.lang.SuppressWarnings("all")
        public BrandBankDetails.BrandBankDetailsBuilder verifiedAt(final OffsetDateTime verifiedAt) {
            this.verifiedAt = verifiedAt;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public BrandBankDetails build() {
            return new BrandBankDetails(this.id, this.brandId, this.accountNumber, this.ifscCode, this.bankRegisteredName, this.nameMatchScore, this.pennyDropStatus, this.verifiedAt);
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        public java.lang.String toString() {
            return "BrandBankDetails.BrandBankDetailsBuilder(id=" + this.id + ", brandId=" + this.brandId + ", accountNumber=" + this.accountNumber + ", ifscCode=" + this.ifscCode + ", bankRegisteredName=" + this.bankRegisteredName + ", nameMatchScore=" + this.nameMatchScore + ", pennyDropStatus=" + this.pennyDropStatus + ", verifiedAt=" + this.verifiedAt + ")";
        }
    }

    @java.lang.SuppressWarnings("all")
    public static BrandBankDetails.BrandBankDetailsBuilder builder() {
        return new BrandBankDetails.BrandBankDetailsBuilder();
    }

    @java.lang.SuppressWarnings("all")
    public UUID getId() {
        return this.id;
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
    public String getBankRegisteredName() {
        return this.bankRegisteredName;
    }

    @java.lang.SuppressWarnings("all")
    public BigDecimal getNameMatchScore() {
        return this.nameMatchScore;
    }

    @java.lang.SuppressWarnings("all")
    public VerificationStatus getPennyDropStatus() {
        return this.pennyDropStatus;
    }

    @java.lang.SuppressWarnings("all")
    public OffsetDateTime getVerifiedAt() {
        return this.verifiedAt;
    }

    @java.lang.SuppressWarnings("all")
    public void setId(final UUID id) {
        this.id = id;
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
    public void setBankRegisteredName(final String bankRegisteredName) {
        this.bankRegisteredName = bankRegisteredName;
    }

    @java.lang.SuppressWarnings("all")
    public void setNameMatchScore(final BigDecimal nameMatchScore) {
        this.nameMatchScore = nameMatchScore;
    }

    @java.lang.SuppressWarnings("all")
    public void setPennyDropStatus(final VerificationStatus pennyDropStatus) {
        this.pennyDropStatus = pennyDropStatus;
    }

    @java.lang.SuppressWarnings("all")
    public void setVerifiedAt(final OffsetDateTime verifiedAt) {
        this.verifiedAt = verifiedAt;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public boolean equals(final java.lang.Object o) {
        if (o == this) return true;
        if (!(o instanceof BrandBankDetails)) return false;
        final BrandBankDetails other = (BrandBankDetails) o;
        if (!other.canEqual((java.lang.Object) this)) return false;
        final java.lang.Object this$id = this.getId();
        final java.lang.Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final java.lang.Object this$brandId = this.getBrandId();
        final java.lang.Object other$brandId = other.getBrandId();
        if (this$brandId == null ? other$brandId != null : !this$brandId.equals(other$brandId)) return false;
        final java.lang.Object this$accountNumber = this.getAccountNumber();
        final java.lang.Object other$accountNumber = other.getAccountNumber();
        if (this$accountNumber == null ? other$accountNumber != null : !this$accountNumber.equals(other$accountNumber)) return false;
        final java.lang.Object this$ifscCode = this.getIfscCode();
        final java.lang.Object other$ifscCode = other.getIfscCode();
        if (this$ifscCode == null ? other$ifscCode != null : !this$ifscCode.equals(other$ifscCode)) return false;
        final java.lang.Object this$bankRegisteredName = this.getBankRegisteredName();
        final java.lang.Object other$bankRegisteredName = other.getBankRegisteredName();
        if (this$bankRegisteredName == null ? other$bankRegisteredName != null : !this$bankRegisteredName.equals(other$bankRegisteredName)) return false;
        final java.lang.Object this$nameMatchScore = this.getNameMatchScore();
        final java.lang.Object other$nameMatchScore = other.getNameMatchScore();
        if (this$nameMatchScore == null ? other$nameMatchScore != null : !this$nameMatchScore.equals(other$nameMatchScore)) return false;
        final java.lang.Object this$pennyDropStatus = this.getPennyDropStatus();
        final java.lang.Object other$pennyDropStatus = other.getPennyDropStatus();
        if (this$pennyDropStatus == null ? other$pennyDropStatus != null : !this$pennyDropStatus.equals(other$pennyDropStatus)) return false;
        final java.lang.Object this$verifiedAt = this.getVerifiedAt();
        final java.lang.Object other$verifiedAt = other.getVerifiedAt();
        if (this$verifiedAt == null ? other$verifiedAt != null : !this$verifiedAt.equals(other$verifiedAt)) return false;
        return true;
    }

    @java.lang.SuppressWarnings("all")
    protected boolean canEqual(final java.lang.Object other) {
        return other instanceof BrandBankDetails;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final java.lang.Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final java.lang.Object $brandId = this.getBrandId();
        result = result * PRIME + ($brandId == null ? 43 : $brandId.hashCode());
        final java.lang.Object $accountNumber = this.getAccountNumber();
        result = result * PRIME + ($accountNumber == null ? 43 : $accountNumber.hashCode());
        final java.lang.Object $ifscCode = this.getIfscCode();
        result = result * PRIME + ($ifscCode == null ? 43 : $ifscCode.hashCode());
        final java.lang.Object $bankRegisteredName = this.getBankRegisteredName();
        result = result * PRIME + ($bankRegisteredName == null ? 43 : $bankRegisteredName.hashCode());
        final java.lang.Object $nameMatchScore = this.getNameMatchScore();
        result = result * PRIME + ($nameMatchScore == null ? 43 : $nameMatchScore.hashCode());
        final java.lang.Object $pennyDropStatus = this.getPennyDropStatus();
        result = result * PRIME + ($pennyDropStatus == null ? 43 : $pennyDropStatus.hashCode());
        final java.lang.Object $verifiedAt = this.getVerifiedAt();
        result = result * PRIME + ($verifiedAt == null ? 43 : $verifiedAt.hashCode());
        return result;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public java.lang.String toString() {
        return "BrandBankDetails(id=" + this.getId() + ", brandId=" + this.getBrandId() + ", accountNumber=" + this.getAccountNumber() + ", ifscCode=" + this.getIfscCode() + ", bankRegisteredName=" + this.getBankRegisteredName() + ", nameMatchScore=" + this.getNameMatchScore() + ", pennyDropStatus=" + this.getPennyDropStatus() + ", verifiedAt=" + this.getVerifiedAt() + ")";
    }

    @java.lang.SuppressWarnings("all")
    public BrandBankDetails() {
    }

    @java.lang.SuppressWarnings("all")
    public BrandBankDetails(final UUID id, final UUID brandId, final String accountNumber, final String ifscCode, final String bankRegisteredName, final BigDecimal nameMatchScore, final VerificationStatus pennyDropStatus, final OffsetDateTime verifiedAt) {
        this.id = id;
        this.brandId = brandId;
        this.accountNumber = accountNumber;
        this.ifscCode = ifscCode;
        this.bankRegisteredName = bankRegisteredName;
        this.nameMatchScore = nameMatchScore;
        this.pennyDropStatus = pennyDropStatus;
        this.verifiedAt = verifiedAt;
    }
}
