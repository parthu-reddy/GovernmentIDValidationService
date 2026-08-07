package com.fooddelivery.governmentid.event;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fooddelivery.common.constants.EventType;
import com.fooddelivery.common.constants.KafkaConstants;
import com.fooddelivery.governmentid.service.BrandVerificationService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class BrandCreatedEventListener {
    @java.lang.SuppressWarnings("all")
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BrandCreatedEventListener.class);
    private final ObjectMapper objectMapper;
    private final BrandVerificationService brandVerificationService;
    private final StringRedisTemplate redisTemplate;

    @KafkaListener(topics = KafkaConstants.TOPIC_RESTAURANT_EVENTS, groupId = KafkaConstants.GROUP_GOV_ID_VALIDATION)
    public void onRestaurantEvent(@Payload String message, @Header("eventType") String eventType) {
        if (EventType.BRAND_CREATED.name().equals(eventType)) {
            try {
                log.info("Received BRAND_CREATED event via Kafka");
                JsonNode payload = objectMapper.readTree(message);
                UUID brandId = UUID.fromString(payload.get("brandId").asText());
                String idempotencyKey = "event:processed:BRAND_CREATED:" + brandId;
                Boolean isNewEvent = redisTemplate.opsForValue().setIfAbsent(idempotencyKey, "1", java.time.Duration.ofDays(7));
                if (!Boolean.TRUE.equals(isNewEvent)) {
                    log.info("BRAND_CREATED event for brand {} already processed. Skipping.", brandId);
                    return;
                }
                String brandName = payload.get("brandName").asText();
                String gstin = payload.get("gstin").asText();
                String bankAccountNumber = payload.get("bankAccountNumber").asText();
                String ifscCode = payload.get("ifscCode").asText();
                // Trigger KYC async locally in the GovID service
                try {
                    brandVerificationService.verifyGstin(brandId, gstin, brandName);
                    brandVerificationService.initiatePennyDrop(brandId, bankAccountNumber, ifscCode, brandName);
                } catch (Exception ex) {
                    // If downstream processing fails, remove idempotency key so we can retry later
                    redisTemplate.delete(idempotencyKey);
                    throw ex;
                }
                log.info("Successfully dispatched KYC checks for Brand: {}", brandId);
            } catch (RuntimeException e) {
                log.error("Transient error processing BRAND_CREATED event, triggering retry", e);
                throw e; // Let Kafka retry mechanism handle it (or DLQ if exhausted)
            } catch (Exception e) {
                log.error("Unrecoverable error processing BRAND_CREATED event in GovernmentIDValidationService", e);
            }
        }
    }

    @java.lang.SuppressWarnings("all")
    public BrandCreatedEventListener(final ObjectMapper objectMapper, final BrandVerificationService brandVerificationService, final StringRedisTemplate redisTemplate) {
        this.objectMapper = objectMapper;
        this.brandVerificationService = brandVerificationService;
        this.redisTemplate = redisTemplate;
    }
}
