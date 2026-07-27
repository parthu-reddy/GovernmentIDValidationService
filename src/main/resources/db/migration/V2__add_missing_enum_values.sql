ALTER TYPE verification_status ADD VALUE IF NOT EXISTS 'APPROVED';
ALTER TYPE verification_status ADD VALUE IF NOT EXISTS 'FAILED';
ALTER TYPE verification_status ADD VALUE IF NOT EXISTS 'MANUAL_REVIEW';

ALTER TYPE document_type ADD VALUE IF NOT EXISTS 'RC';
ALTER TYPE document_type ADD VALUE IF NOT EXISTS 'SELFIE';
ALTER TYPE document_type ADD VALUE IF NOT EXISTS 'GSTIN';
ALTER TYPE document_type ADD VALUE IF NOT EXISTS 'FSSAI';

DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM pg_tables WHERE tablename = 'executive_documents') THEN
        IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'executive_documents' AND column_name = 'doc_type') THEN
            ALTER TABLE executive_documents ADD COLUMN doc_type document_type DEFAULT 'DRIVING_LICENSE';
        END IF;
        UPDATE executive_documents SET doc_type = 'DRIVING_LICENSE' WHERE doc_type IS NULL;
    END IF;
END $$;
