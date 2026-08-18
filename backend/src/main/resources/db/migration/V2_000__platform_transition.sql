-- ============================================================================
-- AIKP Platform
-- Flyway Migration
-- Version : V2.000
-- Aggregate : Platform
-- Description : Align application schemas with the current AIKP architecture
-- ============================================================================

CREATE SCHEMA IF NOT EXISTS metadata;

COMMENT ON SCHEMA metadata
IS 'Questionnaire metadata management';

CREATE SCHEMA IF NOT EXISTS identity;

COMMENT ON SCHEMA identity
IS 'Identity and access management';
