-- ============================================================
-- AIKP Platform
-- V2__iam_roles.sql
-- IAM Roles & User Role FK
-- ============================================================

CREATE TABLE IF NOT EXISTS iam_role (
    id UUID PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL,
    system BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

INSERT INTO iam_role
(id, name, description, status, system, created_at, updated_at)
VALUES
('00000000-0000-0000-0000-000000000001',
 'ADMIN',
 'Platform Administrator',
 'ACTIVE',
 TRUE,
 CURRENT_TIMESTAMP,
 CURRENT_TIMESTAMP),

('00000000-0000-0000-0000-000000000002',
 'COORDINATOR',
 'Regional Campaign Coordinator',
 'ACTIVE',
 TRUE,
 CURRENT_TIMESTAMP,
 CURRENT_TIMESTAMP),

('00000000-0000-0000-0000-000000000003',
 'VALIDATOR',
 'Data Validation Officer',
 'ACTIVE',
 TRUE,
 CURRENT_TIMESTAMP,
 CURRENT_TIMESTAMP),

('00000000-0000-0000-0000-000000000004',
 'DATA_PROVIDER',
 'National Data Provider',
 'ACTIVE',
 TRUE,
 CURRENT_TIMESTAMP,
 CURRENT_TIMESTAMP)
ON CONFLICT (name) DO NOTHING;
