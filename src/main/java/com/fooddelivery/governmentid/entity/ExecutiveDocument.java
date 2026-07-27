package com.fooddelivery.governmentid.entity;

import com.fooddelivery.common.enums.VerificationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.hibernate.type.SqlTypes;

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
    @Column(name = "doc_type", nullable = false, columnDefinition = "document_type")
    private DocumentType docType;

    @Column(nullable = false, length = 100)
    private String documentNumber;

    @Column(length = 512)
    private String documentUrl;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "api_verification_status", columnDefinition = "verification_status")
    private VerificationStatus apiVerificationStatus = VerificationStatus.PENDING;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String apiRawResponse; // Store as string representation of JSON

    private LocalDate expiryDate;

    @CreationTimestamp
    private OffsetDateTime createdAt;
}
