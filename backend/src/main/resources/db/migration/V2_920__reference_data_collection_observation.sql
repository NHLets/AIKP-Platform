-- ============================================================================
-- AIKP Platform
-- Flyway Migration
-- Version : V2.920
-- Aggregate : Reference / Data Collection Observation
-- ============================================================================

CREATE TABLE reference.data_collection_observation
(
    id                        UUID          NOT NULL,
    data_collection_id        UUID          NOT NULL,
    questionnaire_variable_id UUID          NOT NULL,
    reference_year            INTEGER       NOT NULL,

    numeric_value             NUMERIC,
    text_value                TEXT,
    boolean_value             BOOLEAN,
    date_value                DATE,

    observation_status        VARCHAR(30)   NOT NULL,
    selected_unit             VARCHAR(100),
    comment                   TEXT,

    created_at                TIMESTAMPTZ   NOT NULL,
    updated_at                TIMESTAMPTZ,
    created_by                VARCHAR(100),
    updated_by                VARCHAR(100),
    version                   BIGINT        NOT NULL DEFAULT 0,

    CONSTRAINT pk_data_collection_observation
        PRIMARY KEY (id),

    CONSTRAINT fk_data_collection_observation_collection
        FOREIGN KEY (data_collection_id)
        REFERENCES reference.data_collection (id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_data_collection_observation_variable
        FOREIGN KEY (questionnaire_variable_id)
        REFERENCES metadata.questionnaire_variable (id)
        ON DELETE RESTRICT,

    CONSTRAINT ck_data_collection_observation_year
        CHECK (
            reference_year BETWEEN 2015 AND 2025
        ),

    CONSTRAINT uk_data_collection_observation
        UNIQUE (
            data_collection_id,
            questionnaire_variable_id,
            reference_year
        )
);

CREATE INDEX idx_data_collection_observation_collection_id
    ON reference.data_collection_observation (data_collection_id);

CREATE INDEX idx_data_collection_observation_variable_id
    ON reference.data_collection_observation (questionnaire_variable_id);

CREATE INDEX idx_data_collection_observation_reference_year
    ON reference.data_collection_observation (reference_year);

CREATE INDEX idx_data_collection_observation_status
    ON reference.data_collection_observation (observation_status);
