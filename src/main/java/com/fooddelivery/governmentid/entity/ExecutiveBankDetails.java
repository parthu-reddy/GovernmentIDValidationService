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
}
