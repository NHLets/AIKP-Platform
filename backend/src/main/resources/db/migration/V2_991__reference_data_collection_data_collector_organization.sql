-- ============================================================================
-- AIKP Platform
-- Flyway Migration
-- Version : V2.991
-- Aggregate : Reference / Data Collection
-- Purpose : Make data collector an organization rather than a person
-- ============================================================================

ALTER TABLE reference.data_collection
    DROP CONSTRAINT fk_data_collection_person;

DROP INDEX IF EXISTS idx_data_collection_person_id;

ALTER TABLE reference.data_collection
    ADD CONSTRAINT fk_data_collection_data_collector_organization
        FOREIGN KEY (data_collector_id)
        REFERENCES reference.organization(id)
        ON DELETE RESTRICT;

CREATE INDEX idx_data_collection_data_collector_organization_id
    ON reference.data_collection (data_collector_id);
