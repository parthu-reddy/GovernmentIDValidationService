
import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description("should return verification summary")
    request {
        method 'GET'
        urlPath(value(consumer(regex('/api/v1/verification/status/[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}')), producer('/api/v1/verification/status/123e4567-e89b-12d3-a456-426614174000')))
    }
    response {
        status OK()
        headers {
            contentType applicationJson()
        }
        body([
            allDocsApproved: true,
            bankApproved: true,
            dlVehicleClass: 'LMV',
            dlApproved: true,
            rcApproved: true,
            lastBiometricVerificationAt: '2023-10-01T12:00Z'
        ])
    }
}
