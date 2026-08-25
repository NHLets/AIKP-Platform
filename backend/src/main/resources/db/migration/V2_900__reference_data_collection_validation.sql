-- ============================================================================
-- AIKP Platform
-- Flyway Migration
-- Version : V2.900
-- Aggregate : Reference / Data Collection Validation
-- ============================================================================

CREATE TABLE reference.data_collection_validation
(
    id                  UUID          NOT NULL,
    data_collection_id  UUID          NOT NULL,
    validator_id        UUID          NOT NULL,
    decision            VARCHAR(30)   NOT NULL,
    comments            VARCHAR(4000),
    validated_at        TIMESTAMPTZ   NOT NULL,

    CONSTRAINT pk_data_collection_validation
        PRIMARY KEY (id),

    CONSTRAINT fk_data_collection_validation_collection
        FOREIGN KEY (data_collection_id)
        REFERENCES reference.data_collection (id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_data_collection_validation_validator
        FOREIGN KEY (validator_id)
        REFERENCES reference.person (id)
        ON DELETE RESTRICT
);

CREATE INDEX idx_data_collection_validation_collection_id
    ON reference.data_collection_validation (data_collection_id);

CREATE INDEX idx_data_collection_validation_validator_id
    ON reference.data_collection_validation (validator_id);

CREATE INDEX idx_data_collection_validation_decision
    ON reference.data_collection_validation (decision);

CREATE INDEX idx_data_collection_validation_validated_at
    ON reference.data_collection_validation (validated_at);
