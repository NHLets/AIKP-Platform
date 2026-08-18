-- ============================================================================
-- AIKP Platform
-- Flyway Migration
-- Version : V2.300
-- Aggregate : Reference / Organization
-- ============================================================================

CREATE TABLE reference.organization
(
    id             UUID          NOT NULL,
    code           VARCHAR(50)   NOT NULL,
    name           VARCHAR(255)  NOT NULL,
    type           VARCHAR(30)   NOT NULL,
    country_id     UUID          NOT NULL,
    active         BOOLEAN       NOT NULL DEFAULT TRUE,

    created_at     TIMESTAMPTZ   NOT NULL,
    updated_at     TIMESTAMPTZ,
    created_by     VARCHAR(100),
    updated_by     VARCHAR(100),

    version        BIGINT        NOT NULL DEFAULT 0,

    CONSTRAINT pk_organization
        PRIMARY KEY (id),

    CONSTRAINT uk_organization_code
        UNIQUE (code),

    CONSTRAINT fk_organization_country
        FOREIGN KEY (country_id)
        REFERENCES reference.country(id)
);

CREATE INDEX idx_organization_country
    ON reference.organization(country_id);

CREATE INDEX idx_organization_type
    ON reference.organization(type);

CREATE INDEX idx_organization_active
    ON reference.organization(active);
