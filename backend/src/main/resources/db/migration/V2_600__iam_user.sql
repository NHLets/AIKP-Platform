-- ============================================================================
-- AIKP Platform
-- Flyway Migration
-- Version : V2.600
-- Aggregate : IAM / User
-- ============================================================================

CREATE TABLE public.users
(
    id            UUID         NOT NULL,
    username      VARCHAR(50)  NOT NULL,
    email         VARCHAR(254) NOT NULL,
    full_name     VARCHAR(150) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    status        VARCHAR(20)  NOT NULL,
    created_at    TIMESTAMPTZ  NOT NULL,
    updated_at    TIMESTAMPTZ  NOT NULL,
    last_login    TIMESTAMPTZ,

    CONSTRAINT pk_users
        PRIMARY KEY (id),

    CONSTRAINT uk_users_username
        UNIQUE (username),

    CONSTRAINT uk_users_email
        UNIQUE (email)
);
