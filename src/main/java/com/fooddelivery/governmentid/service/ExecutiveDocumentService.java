package com.fooddelivery.governmentid.service;

import com.fooddelivery.governmentid.entity.DocumentType;
import com.fooddelivery.governmentid.entity.ExecutiveDocument;
import com.fooddelivery.common.enums.VerificationStatus;
import com.fooddelivery.governmentid.repository.ExecutiveDocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExecutiveDocumentService {

    private final ExecutiveDocumentRepository documentRepository;

    @Transactional
    public ExecutiveDocument recordVerificationResult(
            UUID executiveId,
            DocumentType docType,
            String docNumber,
            String documentUrl,
            String apiResponse,
            LocalDate expiryDate,
            VerificationStatus status) {

        // Attempt to find existing document of this type to update, or create a new one
        ExecutiveDocument document = documentRepository.findByExecutiveIdAndDocType(executiveId, docType)
                .orElseGet(ExecutiveDocument::new);

        document.setExecutiveId(executiveId);
        document.setDocType(docType);
        document.setDocumentNumber(docNumber);
        document.setDocumentUrl(documentUrl);
        document.setApiRawResponse(apiResponse);
        document.setExpiryDate(expiryDate);
        document.setApiVerificationStatus(status);

        log.info("Recorded verification result for document type {} for executive {}", docType, executiveId);

        return documentRepository.save(document);
    }

    public List<ExecutiveDocument> getDocumentsForExecutive(UUID executiveId) {
        return documentRepository.findByExecutiveId(executiveId);
    }
}
