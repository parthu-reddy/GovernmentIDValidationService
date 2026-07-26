package com.fooddelivery.governmentid.repository;

import com.fooddelivery.governmentid.entity.ExecutiveDocument;
import com.fooddelivery.governmentid.entity.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExecutiveDocumentRepository extends JpaRepository<ExecutiveDocument, UUID> {
    Optional<ExecutiveDocument> findByExecutiveIdAndDocType(UUID executiveId, DocumentType docType);
    List<ExecutiveDocument> findByExecutiveId(UUID executiveId);
}
