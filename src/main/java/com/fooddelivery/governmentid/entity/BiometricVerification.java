package com.fooddelivery.governmentid.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "biometric_verifications", indexes = {@Index(name = "idx_biometric_exec_time", columnList = "executive_id, verification_time DESC")})
public class BiometricVerification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "verification_id")
    private UUID verificationId;
    @Column(name = "executive_id", nullable = false)
    private UUID executiveId;
    @Column(name = "selfie_url", nullable = false, length = 512)
    private String selfieUrl;
    @Column(name = "confidence_score", nullable = false, precision = 4, scale = 3)
    private BigDecimal confidenceScore;
    @Column(name = "is_live", nullable = false)
    private boolean isLive;
    @CreationTimestamp
    @Column(name = "verification_time")
    private OffsetDateTime verificationTime;

    @java.lang.SuppressWarnings("all")
    public UUID getVerificationId() {
        return this.verificationId;
    }

    @java.lang.SuppressWarnings("all")
    public UUID getExecutiveId() {
        return this.executiveId;
    }

    @java.lang.SuppressWarnings("all")
    public String getSelfieUrl() {
        return this.selfieUrl;
    }

    @java.lang.SuppressWarnings("all")
    public BigDecimal getConfidenceScore() {
        return this.confidenceScore;
    }

    @java.lang.SuppressWarnings("all")
    public boolean isLive() {
        return this.isLive;
    }

    @java.lang.SuppressWarnings("all")
    public OffsetDateTime getVerificationTime() {
        return this.verificationTime;
    }

    @java.lang.SuppressWarnings("all")
    public void setVerificationId(final UUID verificationId) {
        this.verificationId = verificationId;
    }

    @java.lang.SuppressWarnings("all")
    public void setExecutiveId(final UUID executiveId) {
        this.executiveId = executiveId;
    }

    @java.lang.SuppressWarnings("all")
    public void setSelfieUrl(final String selfieUrl) {
        this.selfieUrl = selfieUrl;
    }

    @java.lang.SuppressWarnings("all")
    public void setConfidenceScore(final BigDecimal confidenceScore) {
        this.confidenceScore = confidenceScore;
    }

    @java.lang.SuppressWarnings("all")
    public void setLive(final boolean isLive) {
        this.isLive = isLive;
    }

    @java.lang.SuppressWarnings("all")
    public void setVerificationTime(final OffsetDateTime verificationTime) {
        this.verificationTime = verificationTime;
    }
}
