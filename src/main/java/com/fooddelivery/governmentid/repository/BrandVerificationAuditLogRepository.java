package com.fooddelivery.governmentid.repository;

import com.fooddelivery.governmentid.entity.BrandVerificationAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BrandVerificationAuditLogRepository extends JpaRepository<BrandVerificationAuditLog, UUID> {
}
