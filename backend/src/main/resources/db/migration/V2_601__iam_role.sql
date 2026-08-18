-- ============================================================================
-- AIKP Platform
-- Flyway Migration
-- Version : V2.601
-- Aggregate : IAM / Role
-- ============================================================================

CREATE TABLE identity.roles
(
    id          UUID         NOT NULL,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(500) NOT NULL,
    status      VARCHAR(20)  NOT NULL,
    system      BOOLEAN      NOT NULL,
    created_at  TIMESTAMPTZ  NOT NULL,
    updated_at  TIMESTAMPTZ  NOT NULL,

    CONSTRAINT pk_roles
        PRIMARY KEY (id),

    CONSTRAINT uk_roles_name
        UNIQUE (name)
);
