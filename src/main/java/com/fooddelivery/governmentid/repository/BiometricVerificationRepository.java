package com.fooddelivery.governmentid.repository;

import com.fooddelivery.governmentid.entity.BiometricVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BiometricVerificationRepository extends JpaRepository<BiometricVerification, UUID> {

    List<BiometricVerification> findTop10ByExecutiveIdOrderByVerificationTimeDesc(UUID executiveId);

    Optional<BiometricVerification> findTopByExecutiveIdOrderByVerificationTimeDesc(UUID executiveId);
}
