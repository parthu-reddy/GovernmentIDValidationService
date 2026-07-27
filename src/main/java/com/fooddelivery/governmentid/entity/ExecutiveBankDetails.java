package com.fooddelivery.governmentid.entity;

import com.fooddelivery.common.enums.VerificationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "executive_bank_details")
public class ExecutiveBankDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID bankId;

    @Column(name = "executive_id", nullable = false, unique = true)
    private UUID executiveId;

    @Column(nullable = false, length = 50)
    private String accountNumber;

    @Column(nullable = false, length = 20)
    private String ifscCode;

    private String bankRegisteredName;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "penny_drop_status", columnDefinition = "verification_status")
    private VerificationStatus pennyDropStatus = VerificationStatus.PENDING;

    @Column(precision = 4, scale = 3)
    private BigDecimal nameMatchScore;

    private OffsetDateTime verifiedAt;
}
