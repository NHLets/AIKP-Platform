-- ============================================================================
-- AIKP Platform
-- Flyway Migration
-- Version : V2.960
-- Aggregate : Reference / Data Collection
-- Purpose : Add operator organization metadata
-- ============================================================================

ALTER TABLE reference.data_collection
    ADD COLUMN operator_organization_id UUID;

ALTER TABLE reference.data_collection
    ADD CONSTRAINT fk_data_collection_operator_organization
        FOREIGN KEY (operator_organization_id)
        REFERENCES reference.organization(id)
        ON DELETE RESTRICT;

CREATE INDEX idx_data_collection_operator_organization_id
    ON reference.data_collection (operator_organization_id);
