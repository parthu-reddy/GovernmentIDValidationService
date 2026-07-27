package com.fooddelivery.governmentid.entity;

import com.fooddelivery.common.enums.VerificationStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "brand_documents")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrandDocument {

    @Id
    private UUID id;
    
    private UUID brandId;
    
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @jakarta.persistence.Column(name = "doc_type", columnDefinition = "document_type")
    private DocumentType docType; // We will need to add GSTIN, FSSAI to DocumentType
    
    private String documentNumber;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @jakarta.persistence.Column(columnDefinition = "jsonb")
    private String apiRawResponse;
    
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @jakarta.persistence.Column(name = "api_verification_status", columnDefinition = "verification_status")
    private VerificationStatus apiVerificationStatus;
    
    private OffsetDateTime verifiedAt;
}
