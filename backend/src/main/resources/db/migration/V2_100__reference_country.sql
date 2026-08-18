-- ============================================================================
-- AIKP Platform
-- Flyway Migration
-- Version : V2.100
-- Aggregate : Reference / Country
-- Purpose : Create the reference country master data table
-- ============================================================================

CREATE SCHEMA IF NOT EXISTS reference;

COMMENT ON SCHEMA reference
IS 'Reference and master data';

CREATE TABLE reference.country
(
    id             UUID          NOT NULL,
    iso2_code      VARCHAR(2)    NOT NULL,
    iso3_code      VARCHAR(3)    NOT NULL,
    numeric_code   VARCHAR(3)    NOT NULL,
    name           VARCHAR(100)  NOT NULL,
    official_name  VARCHAR(200)  NOT NULL,
    active         BOOLEAN       NOT NULL DEFAULT TRUE,

    created_at     TIMESTAMPTZ   NOT NULL,
    updated_at     TIMESTAMPTZ,
    created_by     VARCHAR(100),
    updated_by     VARCHAR(100),

    version        BIGINT        NOT NULL DEFAULT 0,

    CONSTRAINT pk_country
        PRIMARY KEY (id),

    CONSTRAINT uk_country_iso2_code
        UNIQUE (iso2_code),

    CONSTRAINT uk_country_iso3_code
        UNIQUE (iso3_code),

    CONSTRAINT uk_country_numeric_code
        UNIQUE (numeric_code)
);

CREATE INDEX idx_country_active
    ON reference.country(active);
