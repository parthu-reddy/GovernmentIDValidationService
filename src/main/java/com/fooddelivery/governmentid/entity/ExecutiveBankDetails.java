package com.fooddelivery.governmentid.entity;

import com.fooddelivery.common.enums.VerificationStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "executive_bank_details")
public class ExecutiveBankDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "bank_id")
    private UUID bankId;
    @Column(name = "executive_id", nullable = false, unique = true)
    private UUID executiveId;
    @Column(name = "account_number", nullable = false, length = 50)
    private String accountNumber;
    @Column(name = "ifsc_code", nullable = false, length = 20)
    private String ifscCode;
    @Column(name = "bank_registered_name")
    private String bankRegisteredName;
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "penny_drop_status", columnDefinition = "verification_status")
    private VerificationStatus pennyDropStatus = VerificationStatus.PENDING;
    @Column(name = "name_match_score", precision = 4, scale = 3)
    private BigDecimal nameMatchScore;
    @Column(name = "verified_at")
    private OffsetDateTime verifiedAt;

    @java.lang.SuppressWarnings("all")
    public UUID getBankId() {
        return this.bankId;
    }

    @java.lang.SuppressWarnings("all")
    public UUID getExecutiveId() {
        return this.executiveId;
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
    public VerificationStatus getPennyDropStatus() {
        return this.pennyDropStatus;
    }

    @java.lang.SuppressWarnings("all")
    public BigDecimal getNameMatchScore() {
        return this.nameMatchScore;
    }

    @java.lang.SuppressWarnings("all")
    public OffsetDateTime getVerifiedAt() {
        return this.verifiedAt;
    }

    @java.lang.SuppressWarnings("all")
    public void setBankId(final UUID bankId) {
        this.bankId = bankId;
    }

    @java.lang.SuppressWarnings("all")
    public void setExecutiveId(final UUID executiveId) {
        this.executiveId = executiveId;
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
    public void setPennyDropStatus(final VerificationStatus pennyDropStatus) {
        this.pennyDropStatus = pennyDropStatus;
    }

    @java.lang.SuppressWarnings("all")
    public void setNameMatchScore(final BigDecimal nameMatchScore) {
        this.nameMatchScore = nameMatchScore;
    }

    @java.lang.SuppressWarnings("all")
    public void setVerifiedAt(final OffsetDateTime verifiedAt) {
        this.verifiedAt = verifiedAt;
    }
}
