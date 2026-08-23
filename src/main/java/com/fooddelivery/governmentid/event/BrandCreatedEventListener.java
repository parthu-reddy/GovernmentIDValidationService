package com.fooddelivery.governmentid.event;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fooddelivery.common.constants.EventType;
import com.fooddelivery.common.constants.KafkaConstants;
import com.fooddelivery.common.entity.IdempotencyKey;
import com.fooddelivery.common.repository.IIdempotencyKeyRepository;
import com.fooddelivery.governmentid.service.BrandVerificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;
import java.util.UUID;

@Component
@lombok.extern.slf4j.Slf4j
public class BrandCreatedEventListener {

    private final ObjectMapper objectMapper;
    private final BrandVerificationService brandVerificationService;
    private final IIdempotencyKeyRepository idempotencyKeyRepository;

    public BrandCreatedEventListener(ObjectMapper objectMapper, BrandVerificationService brandVerificationService, IIdempotencyKeyRepository idempotencyKeyRepository) {
        this.objectMapper = objectMapper;
        this.brandVerificationService = brandVerificationService;
        this.idempotencyKeyRepository = idempotencyKeyRepository;
    }

    @RetryableTopic(attempts = "5", backoff = @Backoff(delay = 1000, multiplier = 2.0), autoCreateTopics = "true", dltStrategy = DltStrategy.FAIL_ON_ERROR)
    @KafkaListener(topics = KafkaConstants.TOPIC_RESTAURANT_EVENTS, groupId = KafkaConstants.GROUP_GOV_ID_VALIDATION + "-brandcreatedeventlistener")
    public void onRestaurantEvent(@Payload String message, @Header("eventType") String eventType, @org.springframework.messaging.handler.annotation.Headers java.util.Map<String, Object> headers) {
        if (EventType.BRAND_CREATED.name().equals(eventType)) {
            try {
                log.info("Received BRAND_CREATED event via Kafka");
                JsonNode payload = objectMapper.readTree(message);
                UUID brandId = UUID.fromString(payload.get("brandId").asText());
                
                String extractedEventId = com.fooddelivery.common.util.KafkaHeaderUtils.extractHeaderValue(headers, "eventId");
                final String resolvedEventId;
                if (extractedEventId == null) {
                    resolvedEventId = "BRAND_CREATED:" + brandId.toString();
                } else {
                    resolvedEventId = extractedEventId;
                }
                
                String idempotencyKeyStr = "processed_event:" + resolvedEventId;

                if (idempotencyKeyRepository.existsById(idempotencyKeyStr)) {
                    log.info("Duplicate BRAND_CREATED event ignored: {}", idempotencyKeyStr);
                    return;
                }

                String brandName = payload.get("brandName").asText();
                String gstin = payload.get("gstin").asText();
                String bankAccountNumber = payload.get("bankAccountNumber").asText();
                String ifscCode = payload.get("ifscCode").asText();
                // Trigger KYC async locally in the GovID service
                brandVerificationService.verifyGstin(brandId, gstin, brandName);
                brandVerificationService.initiatePennyDrop(brandId, bankAccountNumber, ifscCode, brandName);
                
                log.info("Successfully dispatched KYC checks for Brand: {}", brandId);

                try {
                    if (!idempotencyKeyRepository.existsById(idempotencyKeyStr)) {
                        idempotencyKeyRepository.save(new IdempotencyKey(idempotencyKeyStr));
                    }
                } catch (Exception e) {
                    log.warn("Failed to save idempotency key {}, but external action was completed", idempotencyKeyStr, e);
                }
            } catch (RuntimeException e) {
                log.error("Transient error processing BRAND_CREATED event, triggering retry", e);
                throw e; // Let Kafka retry mechanism handle it (or DLQ if exhausted)
            } catch (Exception e) {
                log.error("Unrecoverable error processing BRAND_CREATED event in GovernmentIDValidationService", e);
            }
        }
    }

    @DltHandler
    public void handleDlt(Object message, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        System.err.println("Message failed 5 times and sent to DLT: " + topic + " - " + message);
    }
}
