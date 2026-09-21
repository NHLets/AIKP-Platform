-- ============================================================================
-- AIKP Platform
-- Flyway Migration
-- Version : V2.980
-- Aggregate : Reference / Country
-- Purpose : Align the reference country schema with the V2 model
-- ============================================================================

ALTER TABLE reference.country
    ALTER COLUMN version SET DEFAULT 0;

DO $$
BEGIN

    IF EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conrelid = 'reference.country'::regclass
          AND conname = 'uk_country_iso2'
    ) THEN
        ALTER TABLE reference.country
            RENAME CONSTRAINT uk_country_iso2
            TO uk_country_iso2_code;
    END IF;

    IF EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conrelid = 'reference.country'::regclass
          AND conname = 'uk_country_iso3'
    ) THEN
        ALTER TABLE reference.country
            RENAME CONSTRAINT uk_country_iso3
            TO uk_country_iso3_code;
    END IF;

    IF EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conrelid = 'reference.country'::regclass
          AND conname = 'uk_country_numeric'
    ) THEN
        ALTER TABLE reference.country
            RENAME CONSTRAINT uk_country_numeric
            TO uk_country_numeric_code;
    END IF;

END
$$;
