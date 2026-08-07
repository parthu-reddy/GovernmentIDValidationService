package com.fooddelivery.governmentid.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "brand_verification_audit_logs")
public class BrandVerificationAuditLog {
    @Id
    @Column(name = "id")
    private UUID id;
    private String entityType; // e.g. "BRAND"
    private UUID entityId; // e.g. brandId
    private String verificationProvider; // e.g. "KARZA_GSTIN"
    @JdbcTypeCode(SqlTypes.JSON)
    @jakarta.persistence.Column(name = "raw_request_payload", columnDefinition = "jsonb")
    private String rawRequestPayload;
    @JdbcTypeCode(SqlTypes.JSON)
    @jakarta.persistence.Column(name = "raw_response_payload", columnDefinition = "jsonb")
    private String rawResponsePayload;
    @Column(name = "similarity_score")
    private Double similarityScore;
    @Column(name = "status")
    private String status;
    @Column(name = "created_at")
    private LocalDateTime createdAt;


    @java.lang.SuppressWarnings("all")
    public static class BrandVerificationAuditLogBuilder {
        @java.lang.SuppressWarnings("all")
        private UUID id;
        @java.lang.SuppressWarnings("all")
        private String entityType;
        @java.lang.SuppressWarnings("all")
        private UUID entityId;
        @java.lang.SuppressWarnings("all")
        private String verificationProvider;
        @java.lang.SuppressWarnings("all")
        private String rawRequestPayload;
        @java.lang.SuppressWarnings("all")
        private String rawResponsePayload;
        @java.lang.SuppressWarnings("all")
        private Double similarityScore;
        @java.lang.SuppressWarnings("all")
        private String status;
        @java.lang.SuppressWarnings("all")
        private LocalDateTime createdAt;

        @java.lang.SuppressWarnings("all")
        BrandVerificationAuditLogBuilder() {
        }

        /**
         * @return {@code this}.
         */
        @java.lang.SuppressWarnings("all")
        public BrandVerificationAuditLog.BrandVerificationAuditLogBuilder id(final UUID id) {
            this.id = id;
            return this;
        }

        /**
         * @return {@code this}.
         */
        @java.lang.SuppressWarnings("all")
        public BrandVerificationAuditLog.BrandVerificationAuditLogBuilder entityType(final String entityType) {
            this.entityType = entityType;
            return this;
        }

        /**
         * @return {@code this}.
         */
        @java.lang.SuppressWarnings("all")
        public BrandVerificationAuditLog.BrandVerificationAuditLogBuilder entityId(final UUID entityId) {
            this.entityId = entityId;
            return this;
        }

        /**
         * @return {@code this}.
         */
        @java.lang.SuppressWarnings("all")
        public BrandVerificationAuditLog.BrandVerificationAuditLogBuilder verificationProvider(final String verificationProvider) {
            this.verificationProvider = verificationProvider;
            return this;
        }

        /**
         * @return {@code this}.
         */
        @java.lang.SuppressWarnings("all")
        public BrandVerificationAuditLog.BrandVerificationAuditLogBuilder rawRequestPayload(final String rawRequestPayload) {
            this.rawRequestPayload = rawRequestPayload;
            return this;
        }

        /**
         * @return {@code this}.
         */
        @java.lang.SuppressWarnings("all")
        public BrandVerificationAuditLog.BrandVerificationAuditLogBuilder rawResponsePayload(final String rawResponsePayload) {
            this.rawResponsePayload = rawResponsePayload;
            return this;
        }

        /**
         * @return {@code this}.
         */
        @java.lang.SuppressWarnings("all")
        public BrandVerificationAuditLog.BrandVerificationAuditLogBuilder similarityScore(final Double similarityScore) {
            this.similarityScore = similarityScore;
            return this;
        }

        /**
         * @return {@code this}.
         */
        @java.lang.SuppressWarnings("all")
        public BrandVerificationAuditLog.BrandVerificationAuditLogBuilder status(final String status) {
            this.status = status;
            return this;
        }

        /**
         * @return {@code this}.
         */
        @java.lang.SuppressWarnings("all")
        public BrandVerificationAuditLog.BrandVerificationAuditLogBuilder createdAt(final LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public BrandVerificationAuditLog build() {
            return new BrandVerificationAuditLog(this.id, this.entityType, this.entityId, this.verificationProvider, this.rawRequestPayload, this.rawResponsePayload, this.similarityScore, this.status, this.createdAt);
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        public java.lang.String toString() {
            return "BrandVerificationAuditLog.BrandVerificationAuditLogBuilder(id=" + this.id + ", entityType=" + this.entityType + ", entityId=" + this.entityId + ", verificationProvider=" + this.verificationProvider + ", rawRequestPayload=" + this.rawRequestPayload + ", rawResponsePayload=" + this.rawResponsePayload + ", similarityScore=" + this.similarityScore + ", status=" + this.status + ", createdAt=" + this.createdAt + ")";
        }
    }

    @java.lang.SuppressWarnings("all")
    public static BrandVerificationAuditLog.BrandVerificationAuditLogBuilder builder() {
        return new BrandVerificationAuditLog.BrandVerificationAuditLogBuilder();
    }

    @java.lang.SuppressWarnings("all")
    public UUID getId() {
        return this.id;
    }

    @java.lang.SuppressWarnings("all")
    public String getEntityType() {
        return this.entityType;
    }

    @java.lang.SuppressWarnings("all")
    public UUID getEntityId() {
        return this.entityId;
    }

    @java.lang.SuppressWarnings("all")
    public String getVerificationProvider() {
        return this.verificationProvider;
    }

    @java.lang.SuppressWarnings("all")
    public String getRawRequestPayload() {
        return this.rawRequestPayload;
    }

    @java.lang.SuppressWarnings("all")
    public String getRawResponsePayload() {
        return this.rawResponsePayload;
    }

    @java.lang.SuppressWarnings("all")
    public Double getSimilarityScore() {
        return this.similarityScore;
    }

    @java.lang.SuppressWarnings("all")
    public String getStatus() {
        return this.status;
    }

    @java.lang.SuppressWarnings("all")
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    @java.lang.SuppressWarnings("all")
    public void setId(final UUID id) {
        this.id = id;
    }

    @java.lang.SuppressWarnings("all")
    public void setEntityType(final String entityType) {
        this.entityType = entityType;
    }

    @java.lang.SuppressWarnings("all")
    public void setEntityId(final UUID entityId) {
        this.entityId = entityId;
    }

    @java.lang.SuppressWarnings("all")
    public void setVerificationProvider(final String verificationProvider) {
        this.verificationProvider = verificationProvider;
    }

    @java.lang.SuppressWarnings("all")
    public void setRawRequestPayload(final String rawRequestPayload) {
        this.rawRequestPayload = rawRequestPayload;
    }

    @java.lang.SuppressWarnings("all")
    public void setRawResponsePayload(final String rawResponsePayload) {
        this.rawResponsePayload = rawResponsePayload;
    }

    @java.lang.SuppressWarnings("all")
    public void setSimilarityScore(final Double similarityScore) {
        this.similarityScore = similarityScore;
    }

    @java.lang.SuppressWarnings("all")
    public void setStatus(final String status) {
        this.status = status;
    }

    @java.lang.SuppressWarnings("all")
    public void setCreatedAt(final LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public boolean equals(final java.lang.Object o) {
        if (o == this) return true;
        if (!(o instanceof BrandVerificationAuditLog)) return false;
        final BrandVerificationAuditLog other = (BrandVerificationAuditLog) o;
        if (!other.canEqual((java.lang.Object) this)) return false;
        final java.lang.Object this$similarityScore = this.getSimilarityScore();
        final java.lang.Object other$similarityScore = other.getSimilarityScore();
        if (this$similarityScore == null ? other$similarityScore != null : !this$similarityScore.equals(other$similarityScore)) return false;
        final java.lang.Object this$id = this.getId();
        final java.lang.Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final java.lang.Object this$entityType = this.getEntityType();
        final java.lang.Object other$entityType = other.getEntityType();
        if (this$entityType == null ? other$entityType != null : !this$entityType.equals(other$entityType)) return false;
        final java.lang.Object this$entityId = this.getEntityId();
        final java.lang.Object other$entityId = other.getEntityId();
        if (this$entityId == null ? other$entityId != null : !this$entityId.equals(other$entityId)) return false;
        final java.lang.Object this$verificationProvider = this.getVerificationProvider();
        final java.lang.Object other$verificationProvider = other.getVerificationProvider();
        if (this$verificationProvider == null ? other$verificationProvider != null : !this$verificationProvider.equals(other$verificationProvider)) return false;
        final java.lang.Object this$rawRequestPayload = this.getRawRequestPayload();
        final java.lang.Object other$rawRequestPayload = other.getRawRequestPayload();
        if (this$rawRequestPayload == null ? other$rawRequestPayload != null : !this$rawRequestPayload.equals(other$rawRequestPayload)) return false;
        final java.lang.Object this$rawResponsePayload = this.getRawResponsePayload();
        final java.lang.Object other$rawResponsePayload = other.getRawResponsePayload();
        if (this$rawResponsePayload == null ? other$rawResponsePayload != null : !this$rawResponsePayload.equals(other$rawResponsePayload)) return false;
        final java.lang.Object this$status = this.getStatus();
        final java.lang.Object other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) return false;
        final java.lang.Object this$createdAt = this.getCreatedAt();
        final java.lang.Object other$createdAt = other.getCreatedAt();
        if (this$createdAt == null ? other$createdAt != null : !this$createdAt.equals(other$createdAt)) return false;
        return true;
    }

    @java.lang.SuppressWarnings("all")
    protected boolean canEqual(final java.lang.Object other) {
        return other instanceof BrandVerificationAuditLog;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final java.lang.Object $similarityScore = this.getSimilarityScore();
        result = result * PRIME + ($similarityScore == null ? 43 : $similarityScore.hashCode());
        final java.lang.Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final java.lang.Object $entityType = this.getEntityType();
        result = result * PRIME + ($entityType == null ? 43 : $entityType.hashCode());
        final java.lang.Object $entityId = this.getEntityId();
        result = result * PRIME + ($entityId == null ? 43 : $entityId.hashCode());
        final java.lang.Object $verificationProvider = this.getVerificationProvider();
        result = result * PRIME + ($verificationProvider == null ? 43 : $verificationProvider.hashCode());
        final java.lang.Object $rawRequestPayload = this.getRawRequestPayload();
        result = result * PRIME + ($rawRequestPayload == null ? 43 : $rawRequestPayload.hashCode());
        final java.lang.Object $rawResponsePayload = this.getRawResponsePayload();
        result = result * PRIME + ($rawResponsePayload == null ? 43 : $rawResponsePayload.hashCode());
        final java.lang.Object $status = this.getStatus();
        result = result * PRIME + ($status == null ? 43 : $status.hashCode());
        final java.lang.Object $createdAt = this.getCreatedAt();
        result = result * PRIME + ($createdAt == null ? 43 : $createdAt.hashCode());
        return result;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public java.lang.String toString() {
        return "BrandVerificationAuditLog(id=" + this.getId() + ", entityType=" + this.getEntityType() + ", entityId=" + this.getEntityId() + ", verificationProvider=" + this.getVerificationProvider() + ", rawRequestPayload=" + this.getRawRequestPayload() + ", rawResponsePayload=" + this.getRawResponsePayload() + ", similarityScore=" + this.getSimilarityScore() + ", status=" + this.getStatus() + ", createdAt=" + this.getCreatedAt() + ")";
    }

    @java.lang.SuppressWarnings("all")
    public BrandVerificationAuditLog() {
    }

    @java.lang.SuppressWarnings("all")
    public BrandVerificationAuditLog(final UUID id, final String entityType, final UUID entityId, final String verificationProvider, final String rawRequestPayload, final String rawResponsePayload, final Double similarityScore, final String status, final LocalDateTime createdAt) {
        this.id = id;
        this.entityType = entityType;
        this.entityId = entityId;
        this.verificationProvider = verificationProvider;
        this.rawRequestPayload = rawRequestPayload;
        this.rawResponsePayload = rawResponsePayload;
        this.similarityScore = similarityScore;
        this.status = status;
        this.createdAt = createdAt;
    }
}
