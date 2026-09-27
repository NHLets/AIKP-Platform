BEGIN;

-- Supprimer les dépendances dans l'ordre des clés étrangères.

DELETE FROM reference.data_collection;

DELETE FROM campaign.campaign_country;

DELETE FROM reference.person;

DELETE FROM reference.organization;

-- Maintenant les pays peuvent être supprimés.

DELETE FROM reference.country;

-- Burkina Faso
INSERT INTO reference.country (
    id,
    iso2_code,
    iso3_code,
    numeric_code,
    name,
    official_name,
    active,
    created_at,
    version
) VALUES
(
    gen_random_uuid(),
    'BF',
    'BFA',
    '854',
    'Burkina Faso',
    'Burkina Faso',
    true,
    NOW(),
    0
);

-- Burundi
INSERT INTO reference.country (
    id, iso2_code, iso3_code, numeric_code,
    name, official_name, active, created_at, version
) VALUES
(
    gen_random_uuid(),
    'BI',
    'BDI',
    '108',
    'Burundi',
    'Republic of Burundi',
    true,
    NOW(),
    0
);

-- Cameroon
INSERT INTO reference.country (
    id, iso2_code, iso3_code, numeric_code,
    name, official_name, active, created_at, version
) VALUES
(
    gen_random_uuid(),
    'CM',
    'CMR',
    '120',
    'Cameroon',
    'Republic of Cameroon',
    true,
    NOW(),
    0
);

-- Central African Republic
INSERT INTO reference.country (
    id, iso2_code, iso3_code, numeric_code,
    name, official_name, active, created_at, version
) VALUES
(
    gen_random_uuid(),
    'CF',
    'CAF',
    '140',
    'Central African Republic',
    'Central African Republic',
    true,
    NOW(),
    0
);

-- Chad
INSERT INTO reference.country (
    id, iso2_code, iso3_code, numeric_code,
    name, official_name, active, created_at, version
) VALUES
(
    gen_random_uuid(),
    'TD',
    'TCD',
    '148',
    'Chad',
    'Republic of Chad',
    true,
    NOW(),
    0
);

-- Comoros
INSERT INTO reference.country (
    id, iso2_code, iso3_code, numeric_code,
    name, official_name, active, created_at, version
) VALUES
(
    gen_random_uuid(),
    'KM',
    'COM',
    '174',
    'Comoros',
    'Union of the Comoros',
    true,
    NOW(),
    0
);

-- Democratic Republic of the Congo
INSERT INTO reference.country (
    id, iso2_code, iso3_code, numeric_code,
    name, official_name, active, created_at, version
) VALUES
(
    gen_random_uuid(),
    'CD',
    'COD',
    '180',
    'Democratic Republic of the Congo',
    'Democratic Republic of the Congo',
    true,
    NOW(),
    0
);

-- Djibouti
INSERT INTO reference.country (
    id, iso2_code, iso3_code, numeric_code,
    name, official_name, active, created_at, version
) VALUES
(
    gen_random_uuid(),
    'DJ',
    'DJI',
    '262',
    'Djibouti',
    'Republic of Djibouti',
    true,
    NOW(),
    0
);

-- Eritrea
INSERT INTO reference.country (
    id, iso2_code, iso3_code, numeric_code,
    name, official_name, active, created_at, version
) VALUES
(
    gen_random_uuid(),
    'ER',
    'ERI',
    '232',
    'Eritrea',
    'State of Eritrea',
    true,
    NOW(),
    0
);

-- Gambia
INSERT INTO reference.country (
    id, iso2_code, iso3_code, numeric_code,
    name, official_name, active, created_at, version
) VALUES
(
    gen_random_uuid(),
    'GM',
    'GMB',
    '270',
    'Gambia',
    'Republic of The Gambia',
    true,
    NOW(),
    0
);

-- Guinea
INSERT INTO reference.country (
    id, iso2_code, iso3_code, numeric_code,
    name, official_name, active, created_at, version
) VALUES
(
    gen_random_uuid(),
    'GN',
    'GIN',
    '324',
    'Guinea',
    'Republic of Guinea',
    true,
    NOW(),
    0
);

-- Guinea-Bissau
INSERT INTO reference.country (
    id, iso2_code, iso3_code, numeric_code,
    name, official_name, active, created_at, version
) VALUES
(
    gen_random_uuid(),
    'GW',
    'GNB',
    '624',
    'Guinea-Bissau',
    'Republic of Guinea-Bissau',
    true,
    NOW(),
    0
);

-- Liberia
INSERT INTO reference.country (
    id, iso2_code, iso3_code, numeric_code,
    name, official_name, active, created_at, version
) VALUES
(
    gen_random_uuid(),
    'LR',
    'LBR',
    '430',
    'Liberia',
    'Republic of Liberia',
    true,
    NOW(),
    0
);

-- Madagascar
INSERT INTO reference.country (
    id, iso2_code, iso3_code, numeric_code,
    name, official_name, active, created_at, version
) VALUES
(
    gen_random_uuid(),
    'MG',
    'MDG',
    '450',
    'Madagascar',
    'Republic of Madagascar',
    true,
    NOW(),
    0
);

-- Mali
INSERT INTO reference.country (
    id, iso2_code, iso3_code, numeric_code,
    name, official_name, active, created_at, version
) VALUES
(
    gen_random_uuid(),
    'ML',
    'MLI',
    '466',
    'Mali',
    'Republic of Mali',
    true,
    NOW(),
    0
);

-- Mozambique
INSERT INTO reference.country (
    id, iso2_code, iso3_code, numeric_code,
    name, official_name, active, created_at, version
) VALUES
(
    gen_random_uuid(),
    'MZ',
    'MOZ',
    '508',
    'Mozambique',
    'Republic of Mozambique',
    true,
    NOW(),
    0
);

-- Niger
INSERT INTO reference.country (
    id, iso2_code, iso3_code, numeric_code,
    name, official_name, active, created_at, version
) VALUES
(
    gen_random_uuid(),
    'NE',
    'NER',
    '562',
    'Niger',
    'Republic of the Niger',
    true,
    NOW(),
    0
);

-- Sao Tome & Principe
INSERT INTO reference.country (
    id, iso2_code, iso3_code, numeric_code,
    name, official_name, active, created_at, version
) VALUES
(
    gen_random_uuid(),
    'ST',
    'STP',
    '678',
    'Sao Tome & Principe',
    'Democratic Republic of São Tomé and Príncipe',
    true,
    NOW(),
    0
);

-- Sierra Leone
INSERT INTO reference.country (
    id, iso2_code, iso3_code, numeric_code,
    name, official_name, active, created_at, version
) VALUES
(
    gen_random_uuid(),
    'SL',
    'SLE',
    '694',
    'Sierra Leone',
    'Republic of Sierra Leone',
    true,
    NOW(),
    0
);

-- Somalia
INSERT INTO reference.country (
    id, iso2_code, iso3_code, numeric_code,
    name, official_name, active, created_at, version
) VALUES
(
    gen_random_uuid(),
    'SO',
    'SOM',
    '706',
    'Somalia',
    'Federal Republic of Somalia',
    true,
    NOW(),
    0
);

-- South Sudan
INSERT INTO reference.country (
    id, iso2_code, iso3_code, numeric_code,
    name, official_name, active, created_at, version
) VALUES
(
    gen_random_uuid(),
    'SS',
    'SSD',
    '728',
    'South Sudan',
    'Republic of South Sudan',
    true,
    NOW(),
    0
);

-- Sudan
INSERT INTO reference.country (
    id, iso2_code, iso3_code, numeric_code,
    name, official_name, active, created_at, version
) VALUES
(
    gen_random_uuid(),
    'SD',
    'SDN',
    '729',
    'Sudan',
    'Republic of the Sudan',
    true,
    NOW(),
    0
);

-- Togo
INSERT INTO reference.country (
    id, iso2_code, iso3_code, numeric_code,
    name, official_name, active, created_at, version
) VALUES
(
    gen_random_uuid(),
    'TG',
    'TGO',
    '768',
    'Togo',
    'Togolese Republic',
    true,
    NOW(),
    0
);

COMMIT;
