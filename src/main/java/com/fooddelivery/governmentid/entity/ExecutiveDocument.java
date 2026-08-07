package com.fooddelivery.governmentid.entity;

import com.fooddelivery.common.enums.VerificationStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.hibernate.type.SqlTypes;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "executive_documents")
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
    private OffsetDateTime createdAt;

    @java.lang.SuppressWarnings("all")
    public UUID getDocumentId() {
        return this.documentId;
    }

    @java.lang.SuppressWarnings("all")
    public UUID getExecutiveId() {
        return this.executiveId;
    }

    @java.lang.SuppressWarnings("all")
    public DocumentType getDocType() {
        return this.docType;
    }

    @java.lang.SuppressWarnings("all")
    public String getDocumentNumber() {
        return this.documentNumber;
    }

    @java.lang.SuppressWarnings("all")
    public String getDocumentUrl() {
        return this.documentUrl;
    }

    @java.lang.SuppressWarnings("all")
    public VerificationStatus getApiVerificationStatus() {
        return this.apiVerificationStatus;
    }

    @java.lang.SuppressWarnings("all")
    public String getApiRawResponse() {
        return this.apiRawResponse;
    }

    @java.lang.SuppressWarnings("all")
    public LocalDate getExpiryDate() {
        return this.expiryDate;
    }

    @java.lang.SuppressWarnings("all")
    public OffsetDateTime getCreatedAt() {
        return this.createdAt;
    }

    @java.lang.SuppressWarnings("all")
    public void setDocumentId(final UUID documentId) {
        this.documentId = documentId;
    }

    @java.lang.SuppressWarnings("all")
    public void setExecutiveId(final UUID executiveId) {
        this.executiveId = executiveId;
    }

    @java.lang.SuppressWarnings("all")
    public void setDocType(final DocumentType docType) {
        this.docType = docType;
    }

    @java.lang.SuppressWarnings("all")
    public void setDocumentNumber(final String documentNumber) {
        this.documentNumber = documentNumber;
    }

    @java.lang.SuppressWarnings("all")
    public void setDocumentUrl(final String documentUrl) {
        this.documentUrl = documentUrl;
    }

    @java.lang.SuppressWarnings("all")
    public void setApiVerificationStatus(final VerificationStatus apiVerificationStatus) {
        this.apiVerificationStatus = apiVerificationStatus;
    }

    @java.lang.SuppressWarnings("all")
    public void setApiRawResponse(final String apiRawResponse) {
        this.apiRawResponse = apiRawResponse;
    }

    @java.lang.SuppressWarnings("all")
    public void setExpiryDate(final LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    @java.lang.SuppressWarnings("all")
    public void setCreatedAt(final OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
