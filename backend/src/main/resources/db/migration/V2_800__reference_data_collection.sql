-- ============================================================================
-- AIKP Platform
-- Flyway Migration
-- Version : V2.800
-- Aggregate : Reference / Data Collection
-- ============================================================================

CREATE TABLE reference.data_collection
(
    id                          UUID         NOT NULL,
    campaign_id                 UUID         NOT NULL,
    country_id                  UUID         NOT NULL,
    questionnaire_id            UUID         NOT NULL,
    responsible_organization_id UUID         NOT NULL,
    data_collector_id           UUID         NOT NULL,
    status                      VARCHAR(30)  NOT NULL,

    CONSTRAINT pk_data_collection
        PRIMARY KEY (id),

    CONSTRAINT fk_data_collection_campaign
        FOREIGN KEY (campaign_id)
        REFERENCES campaign.campaign (id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_data_collection_country
        FOREIGN KEY (country_id)
        REFERENCES reference.country (id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_data_collection_questionnaire
        FOREIGN KEY (questionnaire_id)
        REFERENCES metadata.questionnaire (id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_data_collection_organization
        FOREIGN KEY (responsible_organization_id)
        REFERENCES reference.organization (id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_data_collection_person
        FOREIGN KEY (data_collector_id)
        REFERENCES reference.person (id)
        ON DELETE RESTRICT
);

CREATE INDEX idx_data_collection_campaign_id
    ON reference.data_collection (campaign_id);

CREATE INDEX idx_data_collection_country_id
    ON reference.data_collection (country_id);

CREATE INDEX idx_data_collection_questionnaire_id
    ON reference.data_collection (questionnaire_id);

CREATE INDEX idx_data_collection_organization_id
    ON reference.data_collection (responsible_organization_id);

CREATE INDEX idx_data_collection_person_id
    ON reference.data_collection (data_collector_id);

CREATE INDEX idx_data_collection_status
    ON reference.data_collection (status);
