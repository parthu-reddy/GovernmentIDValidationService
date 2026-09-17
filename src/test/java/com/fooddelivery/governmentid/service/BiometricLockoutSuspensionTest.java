package com.fooddelivery.governmentid.service;

import com.fooddelivery.governmentid.client.DeliveryExecutiveClient;
import com.fooddelivery.governmentid.entity.BiometricVerification;
import com.fooddelivery.governmentid.repository.BiometricVerificationRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Pins that biometric lockout actually suspends the driver.
 *
 * <p>It previously did not. After three consecutive failed liveness checks the service published
 * EXECUTIVE_SUSPENSION_REQUESTED to `delivery-executive-events` -- a hardcoded topic, absent from
 * KafkaConstants, with no consumer in any service -- logged "Driver {} suspended", and threw. The
 * driver kept delivering. The one test that covered this published the same JSON by hand from its
 * own base class rather than calling the service, so it stayed green throughout.
 *
 * <p>These tests call {@code verifySelfie} and assert on the collaborator, which is what makes them
 * able to fail: revert the fix and the first two go red.
 */
class BiometricLockoutSuspensionTest {

    private final BiometricVerificationRepository repository = mock(BiometricVerificationRepository.class);
    private final DeliveryExecutiveClient deliveryExecutiveClient = mock(DeliveryExecutiveClient.class);
    private final BiometricVerificationService service =
            new BiometricVerificationService(repository, deliveryExecutiveClient);

    private static final UUID EXECUTIVE_ID = UUID.fromString("4f4a4e37-6ca5-5598-94f1-43ef1628f631");

    /** A selfie URL containing "fail" drives simulateBiometricApi to a failing result. */
    private static final String FAILING_SELFIE = "https://cdn.example.com/selfies/fail-01.jpg";

    /**
     * activeProfile is left null: the @Value default only applies under Spring, and null means the
     * service takes the real evaluation path rather than the dev/test bypass that always passes.
     */
    private void givenConsecutiveFailures(int count) {
        List<BiometricVerification> history = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            BiometricVerification failed = new BiometricVerification();
            failed.setExecutiveId(EXECUTIVE_ID);
            failed.setLive(false);
            failed.setConfidenceScore(new BigDecimal("0.650"));
            history.add(failed);
        }
        when(repository.save(any(BiometricVerification.class)))
                .thenAnswer(inv -> inv.getArgument(0));
        when(repository.findTop10ByExecutiveIdOrderByVerificationTimeDesc(EXECUTIVE_ID))
                .thenReturn(history);
    }

    @Test
    void suspendsTheDriverOnTheThirdConsecutiveFailure() {
        givenConsecutiveFailures(3);

        assertThatThrownBy(() -> service.verifySelfie(EXECUTIVE_ID, FAILING_SELFIE))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Maximum biometric retries exceeded");

        verify(deliveryExecutiveClient).suspendDriver(EXECUTIVE_ID.toString());
    }

    @Test
    void doesNotSuspendBeforeTheThirdFailure() {
        givenConsecutiveFailures(2);

        assertThatThrownBy(() -> service.verifySelfie(EXECUTIVE_ID, FAILING_SELFIE))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Retries left");

        verify(deliveryExecutiveClient, never()).suspendDriver(any());
    }

    /**
     * A suspension that did not take must not be reported to the caller as a locked account.
     * The old code caught and logged the publish failure, then threw "Account locked" regardless.
     */
    @Test
    void propagatesSuspensionFailureInsteadOfClaimingTheAccountIsLocked() {
        givenConsecutiveFailures(3);
        when(deliveryExecutiveClient.suspendDriver(EXECUTIVE_ID.toString()))
                .thenThrow(new IllegalStateException("Delivery service is currently unavailable."));

        assertThatThrownBy(() -> service.verifySelfie(EXECUTIVE_ID, FAILING_SELFIE))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Delivery service is currently unavailable")
                .hasMessageNotContaining("Account locked");
    }
}
