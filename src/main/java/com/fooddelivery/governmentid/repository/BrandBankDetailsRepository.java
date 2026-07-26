package com.fooddelivery.governmentid.repository;

import com.fooddelivery.governmentid.entity.BrandBankDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BrandBankDetailsRepository extends JpaRepository<BrandBankDetails, UUID> {
    Optional<BrandBankDetails> findByBrandId(UUID brandId);
}
