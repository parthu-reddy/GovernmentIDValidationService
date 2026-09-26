package com.fooddelivery.governmentid.entity;

import com.fooddelivery.common.enums.VerificationStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.hibernate.type.SqlTypes;
import java.time.LocalDate;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "executive_documents")@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.Data

public class ExecutiveDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "document_id")
    private UUID documentId;
    @Column(name = "executive_id", nullable = false)
    private UUID executiveId;
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "doc_type", nullable = false, columnDefinition = "document_type")
    private DocumentType docType;
    @Column(name = "document_number", nullable = false, length = 100)
    private String documentNumber;
    @Column(name = "document_url", length = 512)
    private String documentUrl;
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "api_verification_status", columnDefinition = "verification_status")
    private VerificationStatus apiVerificationStatus = VerificationStatus.PENDING;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "api_raw_response", columnDefinition = "jsonb")
    private String apiRawResponse; // Store as string representation of JSON
    @Column(name = "expiry_date")
    private LocalDate expiryDate;
    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

}
