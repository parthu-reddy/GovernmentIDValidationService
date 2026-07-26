package com.fooddelivery.governmentid.entity;

import com.fooddelivery.common.enums.VerificationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "executive_documents")
public class ExecutiveDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID documentId;

    @Column(name = "executive_id", nullable = false)
    private UUID executiveId;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(nullable = false)
    private DocumentType docType;

    @Column(nullable = false, length = 100)
    private String documentNumber;

    @Column(length = 512)
    private String documentUrl;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private VerificationStatus apiVerificationStatus = VerificationStatus.PENDING;

    @Column(columnDefinition = "jsonb")
    private String apiRawResponse; // Store as string representation of JSON

    private LocalDate expiryDate;

    @CreationTimestamp
    private OffsetDateTime createdAt;
}
