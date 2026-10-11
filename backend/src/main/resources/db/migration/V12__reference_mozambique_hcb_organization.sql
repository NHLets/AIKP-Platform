-- ============================================================================
-- AIKP Platform
-- Flyway Migration
-- Version : V12
-- Aggregate : Reference / Organization
-- Purpose : Add HCB as an additional utility operator for Mozambique.
-- ============================================================================

INSERT INTO reference.organization (
    id,
    code,
    name,
    type,
    country_id,
    active,
    created_at,
    created_by,
    updated_at,
    updated_by,
    version
)
SELECT
    '00000000-0000-0000-0003-000000000070'::uuid,
    'MOZ-HCB',
    'Hidroelectricade Cahora Bassa.S.A.(HCB)',
    'UTILITY',
    c.id,
    true,
    CURRENT_TIMESTAMP,
    'migration',
    CURRENT_TIMESTAMP,
    'migration',
    0
FROM reference.country c
WHERE c.iso3_code = 'MOZ'
ON CONFLICT (code) DO NOTHING;

DO $$
DECLARE
    hcb_count INTEGER;
BEGIN
    SELECT COUNT(*)
    INTO hcb_count
    FROM reference.organization o
    JOIN reference.country c ON c.id = o.country_id
    WHERE o.code = 'MOZ-HCB'
      AND o.name = 'Hidroelectricade Cahora Bassa.S.A.(HCB)'
      AND o.type = 'UTILITY'
      AND c.iso3_code = 'MOZ';

    IF hcb_count <> 1 THEN
        RAISE EXCEPTION
            'V12 validation failed: expected exactly one Mozambique HCB organization, found %',
            hcb_count;
    END IF;
END $$;
