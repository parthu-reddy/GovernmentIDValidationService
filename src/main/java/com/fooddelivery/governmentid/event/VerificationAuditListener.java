package com.fooddelivery.governmentid.event;

import com.fooddelivery.governmentid.repository.BrandVerificationAuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class VerificationAuditListener {

    private final BrandVerificationAuditLogRepository auditLogRepository;

    @Async
    @EventListener
    public void handleVerificationAuditEvent(VerificationAuditEvent event) {
        log.info("Asynchronously persisting Verification Audit Log for entity: {}", event.getAuditLog().getEntityId());
        try {
            auditLogRepository.save(event.getAuditLog());
        } catch (Exception e) {
            log.error("Failed to persist verification audit log for entity: {}", event.getAuditLog().getEntityId(), e);
        }
    }
}
