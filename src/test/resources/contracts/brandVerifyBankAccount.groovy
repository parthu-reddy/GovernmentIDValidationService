import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description("Should accept a brand bank account verification request")
    request {
        method 'POST'
        url '/api/v1/verification/brands/bank-account'
        headers {
            contentType applicationJson()
        }
        body([
            brandId: $(consumer(regex('[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}')),
                       producer('550e8400-e29b-41d4-a716-446655440000')),
            accountNumber: "1234567890",
            ifscCode: "HDFC0001234",
            brandName: "Pizza Hub"
        ])
    }
    response {
        // BrandVerificationController.verifyBankAccount returns 202 Accepted with no body.
        status ACCEPTED()
    }
}
