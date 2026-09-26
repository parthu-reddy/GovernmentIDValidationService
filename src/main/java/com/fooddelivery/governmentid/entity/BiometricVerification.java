package com.fooddelivery.governmentid.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "biometric_verifications", indexes = {@Index(name = "idx_biometric_exec_time", columnList = "executive_id, verification_time DESC")})@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.Data

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
    private Instant verificationTime;

}
