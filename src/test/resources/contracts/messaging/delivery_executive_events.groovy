package contracts.messaging

/*
 * Real wire payload for delivery-executive-events, from BiometricVerificationService after three
 * consecutive failed biometric checks. Flat, keyed by executiveId, with a body-level eventType.
 */
org.springframework.cloud.contract.spec.Contract.make {
    description("Should publish EXECUTIVE_SUSPENSION_REQUESTED to delivery-executive-events")
    label("delivery_executive_events")
    input {
        triggeredBy('fireExecutiveValidated()')
    }
    outputMessage {
        sentTo('delivery-executive-events')
        body([
            eventType: "EXECUTIVE_SUSPENSION_REQUESTED",
            executiveId: $(producer(regex('[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}')))
        ])
    }
}
