package com.fooddelivery.governmentid.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.Instant;
import java.util.UUID;

import org.springframework.data.domain.Persistable;
import jakarta.persistence.Transient;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostLoad;

@Entity
@Table(name = "brand_verification_audit_logs")@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.Data
@lombok.Builder

public class BrandVerificationAuditLog implements Persistable<UUID> {
    @Id
    @Column(name = "id")
    private UUID id;
    private String entityType; // e.g. "BRAND"
    private UUID entityId; // e.g. brandId
    private String verificationProvider; // e.g. "KARZA_GSTIN"
    @JdbcTypeCode(SqlTypes.JSON)
    @jakarta.persistence.Column(name = "raw_request_payload", columnDefinition = "jsonb")
    private String rawRequestPayload;
    @JdbcTypeCode(SqlTypes.JSON)
    @jakarta.persistence.Column(name = "raw_response_payload", columnDefinition = "jsonb")
    private String rawResponsePayload;
    @Column(name = "similarity_score")
    private Double similarityScore;
    @Column(name = "verification_data")
    private String verificationData;

    @Transient
    private boolean isNew = true;

    @Override
    public boolean isNew() {
        return this.isNew;
    }

    @PostPersist
    @PostLoad
    void markNotNew() {
        this.isNew = false;
    }

    @Column(name = "status")
    private String status;
    @Column(name = "created_at")
    private Instant createdAt;


}
