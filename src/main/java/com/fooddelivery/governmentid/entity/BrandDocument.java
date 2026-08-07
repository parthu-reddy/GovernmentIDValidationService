package com.fooddelivery.governmentid.entity;

import jakarta.persistence.Column;
import com.fooddelivery.common.enums.VerificationStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.hibernate.type.SqlTypes;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "brand_documents")
public class BrandDocument {
    @Id
    @Column(name = "id")
    private UUID id;
    @Column(name = "brand_id")
    private UUID brandId;
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "doc_type", columnDefinition = "document_type")
    private DocumentType docType; // We will need to add GSTIN, FSSAI to DocumentType
    @Column(name = "document_number")
    private String documentNumber;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "api_raw_response", columnDefinition = "jsonb")
    private String apiRawResponse;
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "api_verification_status", columnDefinition = "verification_status")
    private VerificationStatus apiVerificationStatus;
    @Column(name = "verified_at")
    private OffsetDateTime verifiedAt;


    @java.lang.SuppressWarnings("all")
    public static class BrandDocumentBuilder {
        @java.lang.SuppressWarnings("all")
        private UUID id;
        @java.lang.SuppressWarnings("all")
        private UUID brandId;
        @java.lang.SuppressWarnings("all")
        private DocumentType docType;
        @java.lang.SuppressWarnings("all")
        private String documentNumber;
        @java.lang.SuppressWarnings("all")
        private String apiRawResponse;
        @java.lang.SuppressWarnings("all")
        private VerificationStatus apiVerificationStatus;
        @java.lang.SuppressWarnings("all")
        private OffsetDateTime verifiedAt;

        @java.lang.SuppressWarnings("all")
        BrandDocumentBuilder() {
        }

        /**
         * @return {@code this}.
         */
        @java.lang.SuppressWarnings("all")
        public BrandDocument.BrandDocumentBuilder id(final UUID id) {
            this.id = id;
            return this;
        }

        /**
         * @return {@code this}.
         */
        @java.lang.SuppressWarnings("all")
        public BrandDocument.BrandDocumentBuilder brandId(final UUID brandId) {
            this.brandId = brandId;
            return this;
        }

        /**
         * @return {@code this}.
         */
        @java.lang.SuppressWarnings("all")
        public BrandDocument.BrandDocumentBuilder docType(final DocumentType docType) {
            this.docType = docType;
            return this;
        }

        /**
         * @return {@code this}.
         */
        @java.lang.SuppressWarnings("all")
        public BrandDocument.BrandDocumentBuilder documentNumber(final String documentNumber) {
            this.documentNumber = documentNumber;
            return this;
        }

        /**
         * @return {@code this}.
         */
        @java.lang.SuppressWarnings("all")
        public BrandDocument.BrandDocumentBuilder apiRawResponse(final String apiRawResponse) {
            this.apiRawResponse = apiRawResponse;
            return this;
        }

        /**
         * @return {@code this}.
         */
        @java.lang.SuppressWarnings("all")
        public BrandDocument.BrandDocumentBuilder apiVerificationStatus(final VerificationStatus apiVerificationStatus) {
            this.apiVerificationStatus = apiVerificationStatus;
            return this;
        }

        /**
         * @return {@code this}.
         */
        @java.lang.SuppressWarnings("all")
        public BrandDocument.BrandDocumentBuilder verifiedAt(final OffsetDateTime verifiedAt) {
            this.verifiedAt = verifiedAt;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public BrandDocument build() {
            return new BrandDocument(this.id, this.brandId, this.docType, this.documentNumber, this.apiRawResponse, this.apiVerificationStatus, this.verifiedAt);
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        public java.lang.String toString() {
            return "BrandDocument.BrandDocumentBuilder(id=" + this.id + ", brandId=" + this.brandId + ", docType=" + this.docType + ", documentNumber=" + this.documentNumber + ", apiRawResponse=" + this.apiRawResponse + ", apiVerificationStatus=" + this.apiVerificationStatus + ", verifiedAt=" + this.verifiedAt + ")";
        }
    }

    @java.lang.SuppressWarnings("all")
    public static BrandDocument.BrandDocumentBuilder builder() {
        return new BrandDocument.BrandDocumentBuilder();
    }

    @java.lang.SuppressWarnings("all")
    public UUID getId() {
        return this.id;
    }

    @java.lang.SuppressWarnings("all")
    public UUID getBrandId() {
        return this.brandId;
    }

    @java.lang.SuppressWarnings("all")
    public DocumentType getDocType() {
        return this.docType;
    }

    @java.lang.SuppressWarnings("all")
    public String getDocumentNumber() {
        return this.documentNumber;
    }

    @java.lang.SuppressWarnings("all")
    public String getApiRawResponse() {
        return this.apiRawResponse;
    }

    @java.lang.SuppressWarnings("all")
    public VerificationStatus getApiVerificationStatus() {
        return this.apiVerificationStatus;
    }

    @java.lang.SuppressWarnings("all")
    public OffsetDateTime getVerifiedAt() {
        return this.verifiedAt;
    }

    @java.lang.SuppressWarnings("all")
    public void setId(final UUID id) {
        this.id = id;
    }

    @java.lang.SuppressWarnings("all")
    public void setBrandId(final UUID brandId) {
        this.brandId = brandId;
    }

    @java.lang.SuppressWarnings("all")
    public void setDocType(final DocumentType docType) {
        this.docType = docType;
    }

    @java.lang.SuppressWarnings("all")
    public void setDocumentNumber(final String documentNumber) {
        this.documentNumber = documentNumber;
    }

    @java.lang.SuppressWarnings("all")
    public void setApiRawResponse(final String apiRawResponse) {
        this.apiRawResponse = apiRawResponse;
    }

    @java.lang.SuppressWarnings("all")
    public void setApiVerificationStatus(final VerificationStatus apiVerificationStatus) {
        this.apiVerificationStatus = apiVerificationStatus;
    }

    @java.lang.SuppressWarnings("all")
    public void setVerifiedAt(final OffsetDateTime verifiedAt) {
        this.verifiedAt = verifiedAt;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public boolean equals(final java.lang.Object o) {
        if (o == this) return true;
        if (!(o instanceof BrandDocument)) return false;
        final BrandDocument other = (BrandDocument) o;
        if (!other.canEqual((java.lang.Object) this)) return false;
        final java.lang.Object this$id = this.getId();
        final java.lang.Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final java.lang.Object this$brandId = this.getBrandId();
        final java.lang.Object other$brandId = other.getBrandId();
        if (this$brandId == null ? other$brandId != null : !this$brandId.equals(other$brandId)) return false;
        final java.lang.Object this$docType = this.getDocType();
        final java.lang.Object other$docType = other.getDocType();
        if (this$docType == null ? other$docType != null : !this$docType.equals(other$docType)) return false;
        final java.lang.Object this$documentNumber = this.getDocumentNumber();
        final java.lang.Object other$documentNumber = other.getDocumentNumber();
        if (this$documentNumber == null ? other$documentNumber != null : !this$documentNumber.equals(other$documentNumber)) return false;
        final java.lang.Object this$apiRawResponse = this.getApiRawResponse();
        final java.lang.Object other$apiRawResponse = other.getApiRawResponse();
        if (this$apiRawResponse == null ? other$apiRawResponse != null : !this$apiRawResponse.equals(other$apiRawResponse)) return false;
        final java.lang.Object this$apiVerificationStatus = this.getApiVerificationStatus();
        final java.lang.Object other$apiVerificationStatus = other.getApiVerificationStatus();
        if (this$apiVerificationStatus == null ? other$apiVerificationStatus != null : !this$apiVerificationStatus.equals(other$apiVerificationStatus)) return false;
        final java.lang.Object this$verifiedAt = this.getVerifiedAt();
        final java.lang.Object other$verifiedAt = other.getVerifiedAt();
        if (this$verifiedAt == null ? other$verifiedAt != null : !this$verifiedAt.equals(other$verifiedAt)) return false;
        return true;
    }

    @java.lang.SuppressWarnings("all")
    protected boolean canEqual(final java.lang.Object other) {
        return other instanceof BrandDocument;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final java.lang.Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final java.lang.Object $brandId = this.getBrandId();
        result = result * PRIME + ($brandId == null ? 43 : $brandId.hashCode());
        final java.lang.Object $docType = this.getDocType();
        result = result * PRIME + ($docType == null ? 43 : $docType.hashCode());
        final java.lang.Object $documentNumber = this.getDocumentNumber();
        result = result * PRIME + ($documentNumber == null ? 43 : $documentNumber.hashCode());
        final java.lang.Object $apiRawResponse = this.getApiRawResponse();
        result = result * PRIME + ($apiRawResponse == null ? 43 : $apiRawResponse.hashCode());
        final java.lang.Object $apiVerificationStatus = this.getApiVerificationStatus();
        result = result * PRIME + ($apiVerificationStatus == null ? 43 : $apiVerificationStatus.hashCode());
        final java.lang.Object $verifiedAt = this.getVerifiedAt();
        result = result * PRIME + ($verifiedAt == null ? 43 : $verifiedAt.hashCode());
        return result;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public java.lang.String toString() {
        return "BrandDocument(id=" + this.getId() + ", brandId=" + this.getBrandId() + ", docType=" + this.getDocType() + ", documentNumber=" + this.getDocumentNumber() + ", apiRawResponse=" + this.getApiRawResponse() + ", apiVerificationStatus=" + this.getApiVerificationStatus() + ", verifiedAt=" + this.getVerifiedAt() + ")";
    }

    @java.lang.SuppressWarnings("all")
    public BrandDocument() {
    }

    @java.lang.SuppressWarnings("all")
    public BrandDocument(final UUID id, final UUID brandId, final DocumentType docType, final String documentNumber, final String apiRawResponse, final VerificationStatus apiVerificationStatus, final OffsetDateTime verifiedAt) {
        this.id = id;
        this.brandId = brandId;
        this.docType = docType;
        this.documentNumber = documentNumber;
        this.apiRawResponse = apiRawResponse;
        this.apiVerificationStatus = apiVerificationStatus;
        this.verifiedAt = verifiedAt;
    }
}
