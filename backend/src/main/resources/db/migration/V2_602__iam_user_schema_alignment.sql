-- ============================================================================
-- AIKP Platform
-- Flyway Migration
-- Version : V2.602
-- Aggregate : IAM / User
-- Purpose : Align users table with the IAM schema
-- ============================================================================

DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM pg_tables
        WHERE schemaname = 'public'
          AND tablename = 'users'
    )
    AND NOT EXISTS (
        SELECT 1
        FROM pg_tables
        WHERE schemaname = 'identity'
          AND tablename = 'users'
    ) THEN
        ALTER TABLE public.users
            SET SCHEMA identity;
    END IF;
END $$;

-- AIKP Platform 2026
-- Align users table with JPA entity

ALTER TABLE identity.users
    ADD COLUMN IF NOT EXISTS role_id UUID;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'fk_users_role'
    ) THEN
        ALTER TABLE identity.users
        ADD CONSTRAINT fk_users_role
        FOREIGN KEY (role_id)
        REFERENCES identity.roles(id);
    END IF;
END $$;

CREATE INDEX IF NOT EXISTS idx_users_role_id
    ON identity.users(role_id);
