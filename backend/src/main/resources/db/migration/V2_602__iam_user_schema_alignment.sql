-- ============================================================================
-- AIKP Platform
-- Flyway Migration
-- Version : V2.602
-- Aggregate : IAM / User
-- Purpose : Align users table with the IAM schema
-- ============================================================================

ALTER TABLE public.users
    SET SCHEMA identity;
