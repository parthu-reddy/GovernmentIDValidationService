package com.fooddelivery.governmentid.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "brand_verification_audit_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrandVerificationAuditLog {

    @Id
    @Column(name = "id")
    private UUID id;
    
    private String entityType; // e.g. "BRAND"
    private UUID entityId;     // e.g. brandId
    
    private String verificationProvider; // e.g. "KARZA_GSTIN"
    
    @JdbcTypeCode(SqlTypes.JSON)
    @jakarta.persistence.Column(name = "raw_request_payload", columnDefinition = "jsonb")
    private String rawRequestPayload;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @jakarta.persistence.Column(name = "raw_response_payload", columnDefinition = "jsonb")
    private String rawResponsePayload;
    
    @Column(name = "similarity_score")
    private Double similarityScore;
    
    @Column(name = "status")
    private String status;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
