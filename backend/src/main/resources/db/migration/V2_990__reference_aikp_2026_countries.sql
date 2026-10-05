-- AIKP Platform
-- V2.990
-- Reference countries for AIKP TSF 2026 campaign
-- Activate AIKP_TSF_2026 and associate the 23 target countries.
-- Deterministic UUIDs are intentionally used for development reproducibility.

INSERT INTO reference.country (
    id, iso2_code, iso3_code, numeric_code,
    name, official_name, active,
    created_at, created_by, updated_at, updated_by, version
)
VALUES
('00000000-0000-0000-0000-000000000001', 'BF', 'BFA', '854', 'Burkina Faso', 'Burkina Faso', true, CURRENT_TIMESTAMP, 'migration', CURRENT_TIMESTAMP, 'migration', 0),
('00000000-0000-0000-0000-000000000002', 'BI', 'BDI', '108', 'Burundi', 'Republic of Burundi', true, CURRENT_TIMESTAMP, 'migration', CURRENT_TIMESTAMP, 'migration', 0),
('00000000-0000-0000-0000-000000000003', 'CM', 'CMR', '120', 'Cameroon', 'Republic of Cameroon', true, CURRENT_TIMESTAMP, 'migration', CURRENT_TIMESTAMP, 'migration', 0),
('00000000-0000-0000-0000-000000000004', 'CF', 'CAF', '140', 'Central African Republic', 'Central African Republic', true, CURRENT_TIMESTAMP, 'migration', CURRENT_TIMESTAMP, 'migration', 0),
('00000000-0000-0000-0000-000000000005', 'TD', 'TCD', '148', 'Chad', 'Republic of Chad', true, CURRENT_TIMESTAMP, 'migration', CURRENT_TIMESTAMP, 'migration', 0),
('00000000-0000-0000-0000-000000000006', 'KM', 'COM', '174', 'Comoros', 'Union of the Comoros', true, CURRENT_TIMESTAMP, 'migration', CURRENT_TIMESTAMP, 'migration', 0),
('00000000-0000-0000-0000-000000000007', 'CD', 'COD', '180', 'Democratic Republic of the Congo', 'Democratic Republic of the Congo', true, CURRENT_TIMESTAMP, 'migration', CURRENT_TIMESTAMP, 'migration', 0),
('00000000-0000-0000-0000-000000000008', 'DJ', 'DJI', '262', 'Djibouti', 'Republic of Djibouti', true, CURRENT_TIMESTAMP, 'migration', CURRENT_TIMESTAMP, 'migration', 0),
('00000000-0000-0000-0000-000000000009', 'ER', 'ERI', '232', 'Eritrea', 'State of Eritrea', true, CURRENT_TIMESTAMP, 'migration', CURRENT_TIMESTAMP, 'migration', 0),
('00000000-0000-0000-0000-000000000010', 'GN', 'GIN', '324', 'Guinea', 'Republic of Guinea', true, CURRENT_TIMESTAMP, 'migration', CURRENT_TIMESTAMP, 'migration', 0),
('00000000-0000-0000-0000-000000000011', 'GW', 'GNB', '624', 'Guinea-Bissau', 'Republic of Guinea-Bissau', true, CURRENT_TIMESTAMP, 'migration', CURRENT_TIMESTAMP, 'migration', 0),
('00000000-0000-0000-0000-000000000012', 'LR', 'LBR', '430', 'Liberia', 'Republic of Liberia', true, CURRENT_TIMESTAMP, 'migration', CURRENT_TIMESTAMP, 'migration', 0),
('00000000-0000-0000-0000-000000000013', 'MG', 'MDG', '450', 'Madagascar', 'Republic of Madagascar', true, CURRENT_TIMESTAMP, 'migration', CURRENT_TIMESTAMP, 'migration', 0),
('00000000-0000-0000-0000-000000000014', 'ML', 'MLI', '466', 'Mali', 'Republic of Mali', true, CURRENT_TIMESTAMP, 'migration', CURRENT_TIMESTAMP, 'migration', 0),
('00000000-0000-0000-0000-000000000015', 'MZ', 'MOZ', '508', 'Mozambique', 'Republic of Mozambique', true, CURRENT_TIMESTAMP, 'migration', CURRENT_TIMESTAMP, 'migration', 0),
('00000000-0000-0000-0000-000000000016', 'NE', 'NER', '562', 'Niger', 'Republic of the Niger', true, CURRENT_TIMESTAMP, 'migration', CURRENT_TIMESTAMP, 'migration', 0),
('00000000-0000-0000-0000-000000000017', 'ST', 'STP', '678', 'São Tomé and Príncipe', 'Democratic Republic of São Tomé and Príncipe', true, CURRENT_TIMESTAMP, 'migration', CURRENT_TIMESTAMP, 'migration', 0),
('00000000-0000-0000-0000-000000000018', 'SL', 'SLE', '694', 'Sierra Leone', 'Republic of Sierra Leone', true, CURRENT_TIMESTAMP, 'migration', CURRENT_TIMESTAMP, 'migration', 0),
('00000000-0000-0000-0000-000000000019', 'SO', 'SOM', '706', 'Somalia', 'Federal Republic of Somalia', true, CURRENT_TIMESTAMP, 'migration', CURRENT_TIMESTAMP, 'migration', 0),
('00000000-0000-0000-0000-000000000020', 'SS', 'SSD', '728', 'South Sudan', 'Republic of South Sudan', true, CURRENT_TIMESTAMP, 'migration', CURRENT_TIMESTAMP, 'migration', 0),
('00000000-0000-0000-0000-000000000021', 'SD', 'SDN', '729', 'Sudan', 'Republic of the Sudan', true, CURRENT_TIMESTAMP, 'migration', CURRENT_TIMESTAMP, 'migration', 0),
('00000000-0000-0000-0000-000000000022', 'TG', 'TGO', '768', 'Togo', 'Togolese Republic', true, CURRENT_TIMESTAMP, 'migration', CURRENT_TIMESTAMP, 'migration', 0),
('00000000-0000-0000-0000-000000000023', 'GM', 'GMB', '270', 'The Gambia', 'Republic of The Gambia', true, CURRENT_TIMESTAMP, 'migration', CURRENT_TIMESTAMP, 'migration', 0);

UPDATE campaign.campaign
SET
    status = 'ACTIVE',
    updated_at = CURRENT_TIMESTAMP,
    updated_by = 'migration',
    version = version + 1
WHERE code = 'AIKP_TSF_2026';

INSERT INTO campaign.campaign_country (
    id,
    campaign_id,
    country_id,
    created_at,
    created_by,
    updated_at,
    updated_by,
    version
)
SELECT
    v.link_id::uuid,
    cpg.id,
    c.id,
    CURRENT_TIMESTAMP,
    'migration',
    CURRENT_TIMESTAMP,
    'migration',
    0
FROM (
    VALUES
        ('00000000-0000-0000-0001-000000000001', 'BFA'),
        ('00000000-0000-0000-0001-000000000002', 'BDI'),
        ('00000000-0000-0000-0001-000000000003', 'CMR'),
        ('00000000-0000-0000-0001-000000000004', 'CAF'),
        ('00000000-0000-0000-0001-000000000005', 'TCD'),
        ('00000000-0000-0000-0001-000000000006', 'COM'),
        ('00000000-0000-0000-0001-000000000007', 'COD'),
        ('00000000-0000-0000-0001-000000000008', 'DJI'),
        ('00000000-0000-0000-0001-000000000009', 'ERI'),
        ('00000000-0000-0000-0001-000000000010', 'GIN'),
        ('00000000-0000-0000-0001-000000000011', 'GNB'),
        ('00000000-0000-0000-0001-000000000012', 'LBR'),
        ('00000000-0000-0000-0001-000000000013', 'MDG'),
        ('00000000-0000-0000-0001-000000000014', 'MLI'),
        ('00000000-0000-0000-0001-000000000015', 'MOZ'),
        ('00000000-0000-0000-0001-000000000016', 'NER'),
        ('00000000-0000-0000-0001-000000000017', 'STP'),
        ('00000000-0000-0000-0001-000000000018', 'SLE'),
        ('00000000-0000-0000-0001-000000000019', 'SOM'),
        ('00000000-0000-0000-0001-000000000020', 'SSD'),
        ('00000000-0000-0000-0001-000000000021', 'SDN'),
        ('00000000-0000-0000-0001-000000000022', 'TGO'),
        ('00000000-0000-0000-0001-000000000023', 'GMB')
) AS v(link_id, iso3_code)
CROSS JOIN (
    SELECT id
    FROM campaign.campaign
    WHERE code = 'AIKP_TSF_2026'
) AS cpg
JOIN reference.country c
    ON c.iso3_code = v.iso3_code;

DO $$
DECLARE
    country_count INTEGER;
    link_count INTEGER;
    campaign_count INTEGER;
BEGIN
    SELECT COUNT(*)
    INTO country_count
    FROM reference.country
    WHERE iso3_code IN (
        'BFA','BDI','CMR','CAF','TCD','COM','COD','DJI','ERI','GIN','GNB',
        'LBR','MDG','MLI','MOZ','NER','STP','SLE','SOM','SSD','SDN','TGO','GMB'
    );

    IF country_count <> 23 THEN
        RAISE EXCEPTION 'V2.990 validation failed: expected 23 countries, found %', country_count;
    END IF;

    SELECT COUNT(*)
    INTO campaign_count
    FROM campaign.campaign
    WHERE code = 'AIKP_TSF_2026'
      AND status = 'ACTIVE';

    IF campaign_count <> 1 THEN
        RAISE EXCEPTION 'V2.990 validation failed: AIKP_TSF_2026 is not uniquely ACTIVE';
    END IF;

    SELECT COUNT(*)
    INTO link_count
    FROM campaign.campaign_country cc
    JOIN campaign.campaign cp ON cp.id = cc.campaign_id
    WHERE cp.code = 'AIKP_TSF_2026';

    IF link_count <> 23 THEN
        RAISE EXCEPTION 'V2.990 validation failed: expected 23 campaign-country links, found %', link_count;
    END IF;
END $$;
