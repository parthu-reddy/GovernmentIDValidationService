package com.fooddelivery.governmentid.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "biometric_verifications", indexes = {
        @Index(name = "idx_biometric_exec_time", columnList = "executive_id, verification_time DESC")
})
public class BiometricVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID verificationId;

    @Column(name = "executive_id", nullable = false)
    private UUID executiveId;

    @Column(nullable = false, length = 512)
    private String selfieUrl;

    @Column(nullable = false, precision = 4, scale = 3)
    private BigDecimal confidenceScore;

    @Column(nullable = false)
    private boolean isLive;

    @CreationTimestamp
    private OffsetDateTime verificationTime;
}
