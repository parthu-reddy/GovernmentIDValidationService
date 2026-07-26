package com.fooddelivery.governmentid.event;

import com.fooddelivery.governmentid.entity.BrandVerificationAuditLog;
import org.springframework.context.ApplicationEvent;

public class VerificationAuditEvent extends ApplicationEvent {

    private final BrandVerificationAuditLog auditLog;

    public VerificationAuditEvent(Object source, BrandVerificationAuditLog auditLog) {
        super(source);
        this.auditLog = auditLog;
    }

    public BrandVerificationAuditLog getAuditLog() {
        return auditLog;
    }
}
