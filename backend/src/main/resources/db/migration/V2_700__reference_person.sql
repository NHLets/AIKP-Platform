-- ============================================================================
-- AIKP Platform
-- Flyway Migration
-- Version : V2.700
-- Aggregate : Reference / Person
-- ============================================================================

CREATE TABLE reference.person
(
    id                  UUID         NOT NULL,
    full_name           VARCHAR(255) NOT NULL,
    organization_id     UUID         NOT NULL,
    active              BOOLEAN      NOT NULL DEFAULT TRUE,

    CONSTRAINT pk_person
        PRIMARY KEY (id),

    CONSTRAINT fk_person_organization
        FOREIGN KEY (organization_id)
        REFERENCES reference.organization (id)
);

CREATE INDEX idx_person_organization
    ON reference.person (organization_id);

CREATE INDEX idx_person_active
    ON reference.person (active);
