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
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "brand_bank_details")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrandBankDetails {

    @Id
    private UUID id;
    
    private UUID brandId;
    
    private String accountNumber;
    private String ifscCode;
    
    private String bankRegisteredName;
    
    private BigDecimal nameMatchScore;
    
    @Enumerated(EnumType.STRING)
    private VerificationStatus pennyDropStatus;
    
    private OffsetDateTime verifiedAt;
}
