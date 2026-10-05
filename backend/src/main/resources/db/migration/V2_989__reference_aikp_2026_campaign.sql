-- ============================================================================
-- AIKP Platform
-- Flyway Migration
-- Version : V2.989
-- Aggregate : Reference / Campaign
-- Purpose : Seed the AIKP TSF 2026 campaign required by V2.990
-- ============================================================================

INSERT INTO campaign.campaign (
    id,
    code,
    name,
    description,
    start_date,
    end_date,
    status,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES (
    '00000000-0000-0000-0002-000000000001',
    'AIKP_TSF_2026',
    'AIKP data collection for 2026',
    'AIKP data collection campaign for 2026.',
    DATE '2026-08-01',
    DATE '2026-10-31',
    'DRAFT',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'migration',
    'migration',
    0
)
ON CONFLICT (code) DO NOTHING;
