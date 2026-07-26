package com.fooddelivery.governmentid.repository;

import com.fooddelivery.governmentid.entity.BrandDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import com.fooddelivery.governmentid.entity.DocumentType;

@Repository
public interface BrandDocumentRepository extends JpaRepository<BrandDocument, UUID> {
    Optional<BrandDocument> findByBrandIdAndDocType(UUID brandId, DocumentType docType);
}
