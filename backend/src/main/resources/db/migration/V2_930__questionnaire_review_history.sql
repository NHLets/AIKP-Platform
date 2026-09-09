-- ============================================================================
-- AIKP Platform
-- Flyway Migration
-- Version : V2.930
-- Aggregate : Metadata / Questionnaire
-- Purpose : Track whether a questionnaire has previously been submitted for review
-- ============================================================================

ALTER TABLE metadata.questionnaire
    ADD COLUMN previously_submitted_for_review BOOLEAN NOT NULL DEFAULT FALSE;
