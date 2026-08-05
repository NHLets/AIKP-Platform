-- ============================================================================
-- AIKP Platform
-- Flyway Migration
-- Version : V1_100
-- Aggregate : Country
-- ============================================================================

CREATE TABLE reference.country
(

    id                  UUID            NOT NULL,

    iso2_code           VARCHAR(2)      NOT NULL,

    iso3_code           VARCHAR(3)      NOT NULL,

    numeric_code        VARCHAR(3)      NOT NULL,

    name                VARCHAR(100)    NOT NULL,

    official_name       VARCHAR(200)    NOT NULL,

    active              BOOLEAN         NOT NULL DEFAULT TRUE,

    created_at          TIMESTAMPTZ     NOT NULL,

    updated_at          TIMESTAMPTZ,

    created_by          VARCHAR(100),

    updated_by          VARCHAR(100),

    version             BIGINT          NOT NULL,

    CONSTRAINT pk_country
        PRIMARY KEY (id),

    CONSTRAINT uk_country_iso2
        UNIQUE (iso2_code),

    CONSTRAINT uk_country_iso3
        UNIQUE (iso3_code),

    CONSTRAINT uk_country_numeric
        UNIQUE (numeric_code)

);

CREATE INDEX idx_country_name
    ON reference.country(name);

CREATE INDEX idx_country_active
    ON reference.country(active);