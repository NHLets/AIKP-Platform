-- ============================================================================
-- AIKP Platform
-- Flyway Migration
-- Version : V2.992
-- Aggregate : Reference / Organization
-- Purpose : Seed the 69 AIKP TSF 2026 organizations for the 23 target countries.
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
    v.id::uuid,
    v.code,
    v.name,
    v.type,
    c.id,
    true,
    CURRENT_TIMESTAMP,
    'migration',
    CURRENT_TIMESTAMP,
    'migration',
    0
FROM (
    VALUES
        ('00000000-0000-0000-0003-000000000001', 'BFA', 'BFA-NSO', 'National Statistics Office (NSO)', 'STATISTICAL_OFFICE'),
        ('00000000-0000-0000-0003-000000000002', 'BFA', 'BFA-SONABEL', 'SONABEL', 'UTILITY'),
        ('00000000-0000-0000-0003-000000000003', 'BFA', 'BFA-MEMC', 'Ministère de l’Énergie, des Mines et des Carrières (MEMC)', 'MINISTRY'),
        ('00000000-0000-0000-0003-000000000004', 'BDI', 'BDI-NSO', 'National Statistics Office (NSO)', 'STATISTICAL_OFFICE'),
        ('00000000-0000-0000-0003-000000000005', 'BDI', 'BDI-REGIDESO', 'REGIDESO', 'UTILITY'),
        ('00000000-0000-0000-0003-000000000006', 'BDI', 'BDI-MHEM', 'Ministère de l’Hydraulique, de l’Énergie et des Mines (MHEM)', 'MINISTRY'),
        ('00000000-0000-0000-0003-000000000007', 'CMR', 'CMR-NSO', 'National Statistics Office (NSO)', 'STATISTICAL_OFFICE'),
        ('00000000-0000-0000-0003-000000000008', 'CMR', 'CMR-SOCADEL-ENEO', 'SOCADEL/ENEO', 'UTILITY'),
        ('00000000-0000-0000-0003-000000000009', 'CMR', 'CMR-ARSEL', 'Energy Regulatory Agency (ARSEL)', 'REGULATOR'),
        ('00000000-0000-0000-0003-000000000010', 'CAF', 'CAF-NSO', 'National Statistics Office (NSO)', 'STATISTICAL_OFFICE'),
        ('00000000-0000-0000-0003-000000000011', 'CAF', 'CAF-ENERCA', 'ENERCA', 'UTILITY'),
        ('00000000-0000-0000-0003-000000000012', 'CAF', 'CAF-MDE-RH', 'Ministère du Développement de l’Énergie et des Ressources Hydrauliques', 'MINISTRY'),
        ('00000000-0000-0000-0003-000000000013', 'TCD', 'TCD-NSO', 'National Statistics Office (NSO)', 'STATISTICAL_OFFICE'),
        ('00000000-0000-0000-0003-000000000014', 'TCD', 'TCD-SNE', 'SNE', 'UTILITY'),
        ('00000000-0000-0000-0003-000000000015', 'TCD', 'TCD-MEE', 'Ministère de l’Eau et de l’Énergie', 'MINISTRY'),
        ('00000000-0000-0000-0003-000000000016', 'COM', 'COM-NSO', 'National Statistics Office (NSO)', 'STATISTICAL_OFFICE'),
        ('00000000-0000-0000-0003-000000000017', 'COM', 'COM-SONELEC', 'SONELEC', 'UTILITY'),
        ('00000000-0000-0000-0003-000000000018', 'COM', 'COM-MEEH', 'Ministère de l’Énergie, de l’Eau et des Hydrocarbures (MEEH)', 'MINISTRY'),
        ('00000000-0000-0000-0003-000000000019', 'COD', 'COD-NSO', 'National Statistics Office (NSO)', 'STATISTICAL_OFFICE'),
        ('00000000-0000-0000-0003-000000000020', 'COD', 'COD-SNEL', 'SNEL', 'UTILITY'),
        ('00000000-0000-0000-0003-000000000021', 'COD', 'COD-MRHE', 'Ministère des Ressources Hydrauliques et Énergétiques (MRHE)', 'MINISTRY'),
        ('00000000-0000-0000-0003-000000000022', 'DJI', 'DJI-NSO', 'National Statistics Office (NSO)', 'STATISTICAL_OFFICE'),
        ('00000000-0000-0000-0003-000000000023', 'DJI', 'DJI-EDD', 'EDD', 'UTILITY'),
        ('00000000-0000-0000-0003-000000000024', 'DJI', 'DJI-MERN', 'Ministère de l’Énergie, chargé des Ressources Naturelles (MERN)', 'MINISTRY'),
        ('00000000-0000-0000-0003-000000000025', 'ERI', 'ERI-NSO', 'National Statistics Office (NSO)', 'STATISTICAL_OFFICE'),
        ('00000000-0000-0000-0003-000000000026', 'ERI', 'ERI-EEC', 'EEC', 'UTILITY'),
        ('00000000-0000-0000-0003-000000000027', 'ERI', 'ERI-MEM', 'Ministry of Energy and Mines', 'MINISTRY'),
        ('00000000-0000-0000-0003-000000000028', 'GIN', 'GIN-NSO', 'National Statistics Office (NSO)', 'STATISTICAL_OFFICE'),
        ('00000000-0000-0000-0003-000000000029', 'GIN', 'GIN-ELECTRICITE-GUINEE', 'ELECTRICITE DE GUINEE', 'UTILITY'),
        ('00000000-0000-0000-0003-000000000030', 'GIN', 'GIN-ME', 'Ministère de l’Énergie', 'MINISTRY'),
        ('00000000-0000-0000-0003-000000000031', 'GNB', 'GNB-NSO', 'National Statistics Office (NSO)', 'STATISTICAL_OFFICE'),
        ('00000000-0000-0000-0003-000000000032', 'GNB', 'GNB-EAGB', 'EAGB', 'UTILITY'),
        ('00000000-0000-0000-0003-000000000033', 'GNB', 'GNB-MENER', 'Ministério da Energia — MENER', 'MINISTRY'),
        ('00000000-0000-0000-0003-000000000034', 'LBR', 'LBR-NSO', 'Institute of Statistics & Geo-Information Services (NSO)', 'STATISTICAL_OFFICE'),
        ('00000000-0000-0000-0003-000000000035', 'LBR', 'LBR-LEC', 'LEC', 'UTILITY'),
        ('00000000-0000-0000-0003-000000000036', 'LBR', 'LBR-MME', 'Ministry of Mines and Energy (MME)', 'MINISTRY'),
        ('00000000-0000-0000-0003-000000000037', 'MDG', 'MDG-NSO', 'National Statistics Office (NSO)', 'STATISTICAL_OFFICE'),
        ('00000000-0000-0000-0003-000000000038', 'MDG', 'MDG-JIRAMA', 'JIRAMA', 'UTILITY'),
        ('00000000-0000-0000-0003-000000000039', 'MDG', 'MDG-MEH', 'Ministère de l’Énergie et des Hydrocarbures (MEH)', 'MINISTRY'),
        ('00000000-0000-0000-0003-000000000040', 'MLI', 'MLI-NSO', 'National Statistics Office (NSO)', 'STATISTICAL_OFFICE'),
        ('00000000-0000-0000-0003-000000000041', 'MLI', 'MLI-EDM-SA', 'EDM-SA', 'UTILITY'),
        ('00000000-0000-0000-0003-000000000042', 'MLI', 'MLI-MEE', 'Ministère de l’Énergie et de l’Eau (MEE)', 'MINISTRY'),
        ('00000000-0000-0000-0003-000000000043', 'MOZ', 'MOZ-NSO', 'National Statistics Office (NSO)', 'STATISTICAL_OFFICE'),
        ('00000000-0000-0000-0003-000000000044', 'MOZ', 'MOZ-ELECTRICIDADE-MOCAMBIQUE', 'ELECTRICIDADE DE MOCAMBIQUE', 'UTILITY'),
        ('00000000-0000-0000-0003-000000000045', 'MOZ', 'MOZ-MIREME', 'Ministry of Mineral Resources and Energy (MIREME)', 'MINISTRY'),
        ('00000000-0000-0000-0003-000000000046', 'NER', 'NER-NSO', 'National Statistics Office (NSO)', 'STATISTICAL_OFFICE'),
        ('00000000-0000-0000-0003-000000000047', 'NER', 'NER-NIGELEC', 'NIGELEC', 'UTILITY'),
        ('00000000-0000-0000-0003-000000000048', 'NER', 'NER-ME', 'Ministère de l’Énergie (ME)', 'MINISTRY'),
        ('00000000-0000-0000-0003-000000000049', 'STP', 'STP-NSO', 'National Statistics Office (NSO)', 'STATISTICAL_OFFICE'),
        ('00000000-0000-0000-0003-000000000050', 'STP', 'STP-EMAE', 'EMAE', 'UTILITY'),
        ('00000000-0000-0000-0003-000000000051', 'STP', 'STP-MINISTRY-INFRA-NR', 'Ministry of Infrastructure and Natural Resources', 'MINISTRY'),
        ('00000000-0000-0000-0003-000000000052', 'SLE', 'SLE-NSO', 'Statistics Sierra Leone (NSO)', 'STATISTICAL_OFFICE'),
        ('00000000-0000-0000-0003-000000000053', 'SLE', 'SLE-EDSA', 'EDSA', 'UTILITY'),
        ('00000000-0000-0000-0003-000000000054', 'SLE', 'SLE-MOE', 'Ministry of Energy', 'MINISTRY'),
        ('00000000-0000-0000-0003-000000000055', 'SOM', 'SOM-NSO', 'National Statistics Office (NSO)', 'STATISTICAL_OFFICE'),
        ('00000000-0000-0000-0003-000000000056', 'SOM', 'SOM-GECO', 'GECO', 'UTILITY'),
        ('00000000-0000-0000-0003-000000000057', 'SOM', 'SOM-MEWR', 'Ministry of Energy and Water Resources', 'MINISTRY'),
        ('00000000-0000-0000-0003-000000000058', 'SSD', 'SSD-NBS', 'National Bureau of Statistics (NBS)', 'STATISTICAL_OFFICE'),
        ('00000000-0000-0000-0003-000000000059', 'SSD', 'SSD-JEDCO', 'JEDCO', 'UTILITY'),
        ('00000000-0000-0000-0003-000000000060', 'SSD', 'SSD-MOED', 'Ministry of Energy and Dams (MoED)', 'MINISTRY'),
        ('00000000-0000-0000-0003-000000000061', 'SDN', 'SDN-NSO', 'National Statistics Office (NSO)', 'STATISTICAL_OFFICE'),
        ('00000000-0000-0000-0003-000000000062', 'SDN', 'SDN-SEHC', 'SEHC', 'UTILITY'),
        ('00000000-0000-0000-0003-000000000063', 'SDN', 'SDN-MEP', 'Ministry of Energy and Petroleum', 'MINISTRY'),
        ('00000000-0000-0000-0003-000000000064', 'TGO', 'TGO-NSO', 'National Statistics Office (NSO)', 'STATISTICAL_OFFICE'),
        ('00000000-0000-0000-0003-000000000065', 'TGO', 'TGO-CEET', 'CEET', 'UTILITY'),
        ('00000000-0000-0000-0003-000000000066', 'TGO', 'TGO-MDE-RM', 'Ministère délégué chargé de l’Énergie et des Ressources Minières', 'MINISTRY'),
        ('00000000-0000-0000-0003-000000000067', 'GMB', 'GMB-NSO', 'Gambia Bureau of Statistics (NSO)', 'STATISTICAL_OFFICE'),
        ('00000000-0000-0000-0003-000000000068', 'GMB', 'GMB-NAWEC', 'NAWEC', 'UTILITY'),
        ('00000000-0000-0000-0003-000000000069', 'GMB', 'GMB-MOPEM', 'Ministry of Petroleum, Energy and Mines (MOPEM)', 'MINISTRY')
) AS v(id, iso3_code, code, name, type)
JOIN reference.country c
    ON c.iso3_code = v.iso3_code
ON CONFLICT (code) DO NOTHING;

DO $$
DECLARE
    organization_count INTEGER;
    country_organization_count INTEGER;
BEGIN
    SELECT COUNT(*)
    INTO organization_count
    FROM reference.organization o
    JOIN reference.country c ON c.id = o.country_id
    WHERE c.iso3_code IN (
        'BFA','BDI','CMR','CAF','TCD','COM','COD','DJI','ERI','GIN','GNB',
        'LBR','MDG','MLI','MOZ','NER','STP','SLE','SOM','SSD','SDN','TGO','GMB'
    );

    IF organization_count <> 69 THEN
        RAISE EXCEPTION
            'V2.992 validation failed: expected 69 organizations, found %',
            organization_count;
    END IF;

    SELECT COUNT(DISTINCT c.iso3_code)
    INTO country_organization_count
    FROM reference.organization o
    JOIN reference.country c ON c.id = o.country_id
    WHERE c.iso3_code IN (
        'BFA','BDI','CMR','CAF','TCD','COM','COD','DJI','ERI','GIN','GNB',
        'LBR','MDG','MLI','MOZ','NER','STP','SLE','SOM','SSD','SDN','TGO','GMB'
    );

    IF country_organization_count <> 23 THEN
        RAISE EXCEPTION
            'V2.992 validation failed: expected organizations for 23 countries, found %',
            country_organization_count;
    END IF;
END $$;
