package com.fooddelivery.governmentid.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private UUID id;
    
    private String entityType; // e.g. "BRAND"
    private UUID entityId;     // e.g. brandId
    
    private String verificationProvider; // e.g. "KARZA_GSTIN"
    
    @jakarta.persistence.Column(columnDefinition = "jsonb")
    private String rawRequestPayload;
    
    @jakarta.persistence.Column(columnDefinition = "jsonb")
    private String rawResponsePayload;
    
    private Double similarityScore;
    
    private String status;
    private LocalDateTime createdAt;
}
