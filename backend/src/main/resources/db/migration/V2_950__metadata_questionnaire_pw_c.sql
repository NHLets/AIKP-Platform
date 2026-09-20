-- ============================================================================
-- AIKP Platform
-- Flyway Migration
-- Version : V2.950
-- Aggregate : Metadata / Questionnaire / PW_C
-- ============================================================================
--
-- Power Data Template C: Utility Level Data Variables
--
-- AIKP observation period: 2015-2025
-- Source template period: 2022-2005
-- The source template terminology, variable names, definitions and units
-- are preserved. AIKP uses the standardized 2015-2025 observation period.
-- ============================================================================

INSERT INTO metadata.questionnaire
(
    id,
    code,
    name,
    description,
    questionnaire_version,
    default_language,
    status,
    render_type,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    'PW_C',
    'Power Data Template C: Utility Level Data Variables',
    'Utility level access, financial, pricing and technical power sector data questionnaire based on Power Data Template C.',
    '1.0',
    'en',
    'DRAFT',
    'SPREADSHEET',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_group
(
    id,
    questionnaire_id,
    parent_group_id,
    code,
    name,
    description,
    group_type,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    'd2e0718f-51d4-5477-916c-0643bdece93b',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    NULL,
    'ACCESS',
    'Access (ACC)',
    'Access data variables from Power Data Template C.',
    'POLICY_GROUP',
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_group
(
    id,
    questionnaire_id,
    parent_group_id,
    code,
    name,
    description,
    group_type,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    '57c9d6b9-8519-5997-a980-a51e7c439897',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    NULL,
    'FINANCIAL',
    'Financial (FIN)',
    'Financial data variables from Power Data Template C.',
    'POLICY_GROUP',
    2,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_group
(
    id,
    questionnaire_id,
    parent_group_id,
    code,
    name,
    description,
    group_type,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    '3d01c090-48f3-5e06-a79a-2f674c1393f6',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    NULL,
    'PRICING',
    'Pricing (PRI)',
    'Pricing data variables from Power Data Template C.',
    'POLICY_GROUP',
    3,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_group
(
    id,
    questionnaire_id,
    parent_group_id,
    code,
    name,
    description,
    group_type,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    '9307fd64-9bde-5e73-995b-46a9d6f37d67',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    NULL,
    'TECHNICAL',
    'Technical (TEC)',
    'Technical data variables from Power Data Template C.',
    'POLICY_GROUP',
    4,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    'c72020d3-65bb-52eb-9010-e95ec4233567',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    'd2e0718f-51d4-5477-916c-0643bdece93b',
    'a192',
    'Customers',
    'Residential customers in utility service area (connected to power)',
    'NUMBER',
    '# customers',
    TRUE,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    '5a59ab64-2eb6-54f5-ba16-3e91508879b7',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    'd2e0718f-51d4-5477-916c-0643bdece93b',
    'a195',
    'Residential Customers',
    'Residential customers in utility service area (connected to power)',
    'NUMBER',
    '# customers',
    TRUE,
    2,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    '430ddf7a-a635-5946-ba5a-58a239405efd',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    'd2e0718f-51d4-5477-916c-0643bdece93b',
    'a197',
    'Non-residential Customers',
    'Non-residential customers in utility service area (connected to power)',
    'NUMBER',
    '# customers',
    TRUE,
    3,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    'bf9f6c1e-61cc-54d8-bdb3-d1f73874ddd7',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    'd2e0718f-51d4-5477-916c-0643bdece93b',
    'a199',
    'Commercial Customers',
    'Non-residential commercial customers in utility service area (connected to power)',
    'NUMBER',
    '# customers',
    TRUE,
    4,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    '348dc9f1-82c7-5782-a822-573e66c51e1d',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    'd2e0718f-51d4-5477-916c-0643bdece93b',
    'a201',
    'Industrial Customers',
    'Non-residential industrial customers in utility service area (connected to power)',
    'NUMBER',
    '# customers',
    TRUE,
    5,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    '663dc611-1375-547f-bf00-99076d02dc1f',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    'd2e0718f-51d4-5477-916c-0643bdece93b',
    'a203',
    'LV Customers',
    'LV customers in utility service area (connected to power)',
    'NUMBER',
    '# customers',
    TRUE,
    6,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    'e5fac226-c0e3-57ff-ab50-3c48b261a012',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    'd2e0718f-51d4-5477-916c-0643bdece93b',
    'a205',
    'MV Customers',
    'MV customers in utility service area (connected to power)',
    'NUMBER',
    '# customers',
    TRUE,
    7,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    'b3d9b312-32af-574c-8426-37877ad8dad2',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    'd2e0718f-51d4-5477-916c-0643bdece93b',
    'a207',
    'HV Customers',
    'HV customers in utility service area (connected to power)',
    'NUMBER',
    '# customers',
    TRUE,
    8,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    'b5658bf9-a3f4-5afb-a860-319af29c1c95',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    'd2e0718f-51d4-5477-916c-0643bdece93b',
    'a214',
    'Potential Customers',
    'Total potential customers in utility service area (not connected to power, but with technical possibility to be connected)',
    'NUMBER',
    '# customers',
    TRUE,
    9,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    'adafb9a9-63a8-5d9d-ac05-8433c4bf25ae',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    'd2e0718f-51d4-5477-916c-0643bdece93b',
    'a261',
    'Residential Potential Customers',
    'Residential potential customers in utility service area (not connected to power, but with technical possibility to be connected)',
    'NUMBER',
    '# customers',
    TRUE,
    10,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    '1870072e-93bd-5cbc-b6fa-6f5ff3f91ebc',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    'd2e0718f-51d4-5477-916c-0643bdece93b',
    'a262',
    'Non-Residential Potential Customers',
    'Non-residential potential customers in utility service area (not connected to power, but with technical possibility to be connected)',
    'NUMBER',
    '# customers',
    TRUE,
    11,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    '0a56e3ec-cc84-52a2-bc68-068df4b5c2c7',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    'd2e0718f-51d4-5477-916c-0643bdece93b',
    'b048',
    'Metered Customers',
    'Number of residential customers with installed meter',
    'NUMBER',
    '# customers',
    TRUE,
    12,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    'c4aa19be-1315-5797-811c-9d8e89a2de5f',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    'd2e0718f-51d4-5477-916c-0643bdece93b',
    'b049',
    'Metered Customers - Operating',
    'Number of residential customers with installed and operating meter',
    'NUMBER',
    '# customers',
    TRUE,
    13,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    'e985caf3-d48b-5e31-9cd2-9fb1816eb7a6',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    'd2e0718f-51d4-5477-916c-0643bdece93b',
    'b050',
    'Prepayment Customers',
    'Number of residential customers with installed prepayment meters',
    'NUMBER',
    '# customers',
    TRUE,
    14,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    'c81253d7-44dc-5862-aab3-dca13a10526f',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    'd2e0718f-51d4-5477-916c-0643bdece93b',
    'b051',
    'Prepayment Customers - Operating',
    'Number of residential customers with installed and operating prepayment meters',
    'NUMBER',
    '# customers',
    TRUE,
    15,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    'e38b79e2-1d27-5b37-826e-30f1587f2e65',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    '57c9d6b9-8519-5997-a980-a51e7c439897',
    'b061',
    'Collected bills',
    'Total Revenue Collected from Sold Electricity (annual):',
    'NUMBER',
    'LCU per year',
    TRUE,
    16,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    'fe09e918-72dd-5c08-8ae9-303e1a1d9764',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    '57c9d6b9-8519-5997-a980-a51e7c439897',
    'b062',
    'Collected Bills Residential Customers',
    'Total Revenue Collected from Sold Electricity (annual):residential',
    'NUMBER',
    'LCU per year',
    TRUE,
    17,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    '397ecd8d-bf22-5fa8-8866-52735b02f31f',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    '57c9d6b9-8519-5997-a980-a51e7c439897',
    'b063',
    'Collected Bills Commercial Customers',
    'Total Revenue Collected from Sold Electricity (annual):commercial',
    'NUMBER',
    'LCU per year',
    TRUE,
    18,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    '01b4933f-47a5-5b91-abcb-0d10569161a1',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    '57c9d6b9-8519-5997-a980-a51e7c439897',
    'b064',
    'Collected Bills Indusrial Customers',
    'Total Revenue Collected from Sold Electricity (annual):industrial',
    'NUMBER',
    'LCU per year',
    TRUE,
    19,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    '3d1780d6-d927-57aa-886f-a4ee6b4c3e25',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    '57c9d6b9-8519-5997-a980-a51e7c439897',
    'b065',
    'Collected Bills LV Customers',
    'Total Revenue Collected from Sold Electricity (annual):LV',
    'NUMBER',
    'LCU per year',
    TRUE,
    20,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    'b1f43cdd-f87b-5118-9055-1a7cce455193',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    '57c9d6b9-8519-5997-a980-a51e7c439897',
    'b066',
    'Collected Bills MV Customers',
    'Total Revenue Collected from Sold Electricity (annual):MV',
    'NUMBER',
    'LCU per year',
    TRUE,
    21,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    'a1e086cf-54eb-5231-a908-e5b3ea595f26',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    '57c9d6b9-8519-5997-a980-a51e7c439897',
    'b067',
    'Collected Bills HV Customers',
    'Total Revenue Collected from Sold Electricity (annual):HV',
    'NUMBER',
    'LCU per year',
    TRUE,
    22,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    '729a90b0-fc1d-5b2b-87aa-dc1582f13901',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    '57c9d6b9-8519-5997-a980-a51e7c439897',
    'b069',
    'Billing of electricity',
    'Total Electricity Billed, annual',
    'NUMBER',
    'LCU per year',
    TRUE,
    23,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    'e6e3d22d-4f53-5475-8048-3185157fd99a',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    '57c9d6b9-8519-5997-a980-a51e7c439897',
    'b070',
    'Billing  to Residential Customers',
    'Total Electricity Billed, annual residential',
    'NUMBER',
    'LCU per year',
    TRUE,
    24,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    '11d5f295-16eb-5193-a0a4-62366b6a4380',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    '57c9d6b9-8519-5997-a980-a51e7c439897',
    'b071',
    'Billing  to Commercial Customers',
    'Total Electricity Billed, annual commercial',
    'NUMBER',
    'LCU per year',
    TRUE,
    25,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    '38f2a6be-4635-5539-aed6-22f0bef8f746',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    '57c9d6b9-8519-5997-a980-a51e7c439897',
    'b072',
    'Billing  to Indusrial Customers',
    'Total Electricity Billed, annual industrial',
    'NUMBER',
    'LCU per year',
    TRUE,
    26,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    '0153f435-5f8b-5cbe-8f89-345d15faaa5a',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    '57c9d6b9-8519-5997-a980-a51e7c439897',
    'b073',
    'Billing  to LV Customers',
    'Total Electricity Billed, annual LV',
    'NUMBER',
    'LCU per year',
    TRUE,
    27,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    '9a43177f-2682-5bd5-8f32-19cca238d548',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    '57c9d6b9-8519-5997-a980-a51e7c439897',
    'b074',
    'Billing  to MV Customers',
    'Total Electricity Billed, annual MV',
    'NUMBER',
    'LCU per year',
    TRUE,
    28,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    'b43e523c-5995-5af9-8ef3-ba04097ce1b2',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    '57c9d6b9-8519-5997-a980-a51e7c439897',
    'b075',
    'Billing to HV Customers',
    'Total Electricity Billed, annual HV',
    'NUMBER',
    'LCU per year',
    TRUE,
    29,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    '776251a1-b2fd-5f26-8a96-d13657056200',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    '57c9d6b9-8519-5997-a980-a51e7c439897',
    'b076',
    'Operational Costs',
    'Total Operational Costs per year (excluding depreciation and debt service)',
    'NUMBER',
    'LCU per year',
    TRUE,
    30,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    '22e96499-5129-5abf-babb-76340957c5d6',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    '57c9d6b9-8519-5997-a980-a51e7c439897',
    'b077',
    'Labor Costs',
    'Out of which: Labor costs',
    'NUMBER',
    'LCU per year',
    TRUE,
    31,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    '7dc9061e-4c42-5df6-ae98-b534a3c4b814',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    '57c9d6b9-8519-5997-a980-a51e7c439897',
    'b078',
    'Fuel Costs',
    'Out of which: Fuel costs',
    'NUMBER',
    'LCU per year',
    TRUE,
    32,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    '348fe89d-8a83-5dfd-96cb-801a66a3eeda',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    '57c9d6b9-8519-5997-a980-a51e7c439897',
    'b079',
    'Maintenance Costs',
    'Out of which: Maintenance',
    'NUMBER',
    'LCU per year',
    TRUE,
    33,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    '7a3984ef-9c1f-5b2c-9fb5-678a86288165',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    '57c9d6b9-8519-5997-a980-a51e7c439897',
    'b081',
    'Capital Cost',
    'Total Capital Expenses, annual',
    'NUMBER',
    'LCU per year',
    TRUE,
    34,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    'a8d1ea19-0f41-5e2e-ab37-43eda56ab708',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    '57c9d6b9-8519-5997-a980-a51e7c439897',
    'b082',
    'Rehabilitation Costs',
    'Out of which: Rehabilitation',
    'NUMBER',
    'LCU per year',
    TRUE,
    35,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    '959932f8-cdb0-5bf3-822e-8346d68417f5',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    '57c9d6b9-8519-5997-a980-a51e7c439897',
    'b083',
    'New Assets Investment',
    'Out of which: Investment (non-financial)',
    'NUMBER',
    'LCU per year',
    TRUE,
    36,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    'dfc5a118-1c39-5779-a370-2ebd182d6329',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    '57c9d6b9-8519-5997-a980-a51e7c439897',
    'b085',
    'Deb Service',
    'Debt service expenditure, annual',
    'NUMBER',
    'LCU per year',
    TRUE,
    37,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    'd3d1541f-ae3f-5489-86f0-9aaf03519f72',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    '57c9d6b9-8519-5997-a980-a51e7c439897',
    'b086',
    'Asset Value',
    'Total book value of gross fixed assets, annual',
    'NUMBER',
    'LCU per year',
    TRUE,
    38,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    '15fb0146-6c48-5775-a1c1-4032535234ad',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    '57c9d6b9-8519-5997-a980-a51e7c439897',
    'b206',
    'Revenue, total',
    'Total Utility Revenue (annual):',
    'NUMBER',
    'LCU',
    TRUE,
    39,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    'ffdb750b-6de5-5176-8170-316d4a2b1e66',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    '57c9d6b9-8519-5997-a980-a51e7c439897',
    'b243',
    'Billing of electricity to government entities',
    'Billing of electricity to government entities',
    'NUMBER',
    'LCU per year',
    TRUE,
    40,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    '9dba4afc-944e-5776-86be-4119e756af01',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    '3d01c090-48f3-5e06-a79a-2f674c1393f6',
    'b059',
    'Connection charge, residential customers',
    'Connection charge, residential customers',
    'NUMBER',
    'LCU per connection',
    TRUE,
    41,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    'aaa9235c-fe73-5735-ba48-1cde5f6d074d',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    '3d01c090-48f3-5e06-a79a-2f674c1393f6',
    'b060',
    'Connection charge, medium voltage customer',
    'Connection charge, medium voltage customer',
    'NUMBER',
    'LCU per connection',
    TRUE,
    42,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    'dccf98f8-10bb-5217-b6a3-993cebd8b5b9',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    '3d01c090-48f3-5e06-a79a-2f674c1393f6',
    'b179',
    'Connection charge, medium voltage customer',
    'Connection charge, medium voltage customer',
    'NUMBER',
    'US$ per connection',
    TRUE,
    43,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    '131f65ec-0fff-5e98-bcb2-81f41013538b',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    '3d01c090-48f3-5e06-a79a-2f674c1393f6',
    'b237',
    'Tariff, average effective',
    'Tariff, average effective',
    'NUMBER',
    'LCU per kWh',
    TRUE,
    44,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    'e4a37675-ddec-5789-9e22-4954e5e8e0b6',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    '3d01c090-48f3-5e06-a79a-2f674c1393f6',
    'b240',
    'Fixed charge',
    'Fixed charge',
    'NUMBER',
    'LCU per month',
    TRUE,
    45,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    '9e099fa7-e5c5-5384-80e2-17c0be351050',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    '9307fd64-9bde-5e73-995b-46a9d6f37d67',
    'a191',
    'Utility Area',
    'Utility Area',
    'NUMBER',
    'square km',
    TRUE,
    46,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    '642194d1-5a0b-51e4-86c4-213d0f371e26',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    '9307fd64-9bde-5e73-995b-46a9d6f37d67',
    'b052',
    'System Losses',
    'System Losses',
    'NUMBER',
    'KWh, MWh or GWh per year',
    TRUE,
    47,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    'b0e232b0-10da-504a-8f27-eb5d5339c141',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    '9307fd64-9bde-5e73-995b-46a9d6f37d67',
    'b053',
    'Technical Losses',
    'Technical Losses',
    'NUMBER',
    'KWh, MWh or GWh per year',
    TRUE,
    48,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    'c9193e4a-b58f-56b5-8337-8009754e8583',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    '9307fd64-9bde-5e73-995b-46a9d6f37d67',
    'b054',
    'Non-Technical Losses',
    'Non-Technical Losses',
    'NUMBER',
    'KWh, MWh or GWh per year',
    TRUE,
    49,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    '0f2ef87b-3876-5fc3-93bc-ba3463661c68',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    '9307fd64-9bde-5e73-995b-46a9d6f37d67',
    'b055',
    'Transmission losses',
    'Transmission losses',
    'NUMBER',
    'KWh, MWh or GWh per year',
    TRUE,
    50,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    '10395131-e588-5c7e-a509-9e897c7a899c',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    '9307fd64-9bde-5e73-995b-46a9d6f37d67',
    'b056',
    'Distribution losses',
    'Distribution losses',
    'NUMBER',
    'KWh, MWh or GWh per year',
    TRUE,
    51,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    'ce55df9c-2136-5fcd-be8f-8a0967a044c2',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    '9307fd64-9bde-5e73-995b-46a9d6f37d67',
    'b057',
    'Employees',
    'Total number of employees',
    'NUMBER',
    '# people/year',
    TRUE,
    52,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    '609ef076-31dc-5fed-b457-bb2bebe5ab5c',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    '9307fd64-9bde-5e73-995b-46a9d6f37d67',
    'b234',
    'Electricity sold, volume',
    'Electricity sold, volume',
    'NUMBER',
    'GWh per year',
    TRUE,
    53,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    updated_at,
    created_by,
    updated_by,
    version
)
VALUES
(
    '799a5888-07a6-5d61-a7eb-d47a0b1e1403',
    '5c18299c-b9ca-56f4-8f48-918dbc734c63',
    '9307fd64-9bde-5e73-995b-46a9d6f37d67',
    'b235',
    'Electricity generated, volume',
    'Electricity generated, volume',
    'NUMBER',
    'GWh per year',
    TRUE,
    54,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);
