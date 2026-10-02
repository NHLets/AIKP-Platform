-- ============================================================
-- AIKP Platform
-- V2.604__iam_roles_realign.sql
-- Align legacy/reference IAM roles with identity.roles
-- ============================================================

INSERT INTO identity.roles
(
    id,
    name,
    description,
    status,
    system,
    created_at,
    updated_at
)
SELECT
    id,
    name,
    description,
    status,
    system,
    created_at,
    updated_at
FROM reference.iam_role
ON CONFLICT (id) DO UPDATE
SET
    name        = EXCLUDED.name,
    description = EXCLUDED.description,
    status      = EXCLUDED.status,
    system      = EXCLUDED.system,
    updated_at  = EXCLUDED.updated_at;
