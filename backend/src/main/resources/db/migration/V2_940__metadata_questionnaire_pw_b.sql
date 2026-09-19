-- ============================================================================
-- AIKP Platform
-- Flyway Migration
-- Version : V2.940
-- Aggregate : Metadata / Questionnaire / PW_B
-- ============================================================================
--
-- Power Data Template B: National Level Data Variables
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
    '45b2179c-6214-5b12-972f-1068923578c1',
    'PW_B',
    'Power Data Template B: National Level Data Variables',
    'National level technical power sector data questionnaire based on Power Data Template B.',
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
    '17b039a9-5643-518f-9d61-0da5ae11609f',
    '45b2179c-6214-5b12-972f-1068923578c1',
    NULL,
    'TECHNICAL',
    'Technical (TEC)',
    'Technical data variables from Power Data Template B.',
    'POLICY_GROUP',
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
    '13c20cf9-58c8-55fc-8d3b-4d6efd639ec2',
    '45b2179c-6214-5b12-972f-1068923578c1',
    '17b039a9-5643-518f-9d61-0da5ae11609f',
    'b001',
    'Installed Generation Capacity',
    'Total capacity of the interconnected grid in hydro-electric, conventional thermal, nuclear, and solar, wind, biomass, geothermal. This variable includes IPP generation capacity but excludes emergency generation and self-generation capacities.',
    'NUMBER',
    'KW, MW, GW',
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
    '2e208ad5-474b-5956-b2bb-7c9a9abb8705',
    '45b2179c-6214-5b12-972f-1068923578c1',
    '17b039a9-5643-518f-9d61-0da5ae11609f',
    'b002',
    'Generation capacity hydro-electric',
    'Capacity of hydro-electric plants on the interconnected grid',
    'NUMBER',
    'KW, MW, GW',
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
    '17cf7f8a-46a3-5ea0-9b03-b6beae03c19a',
    '45b2179c-6214-5b12-972f-1068923578c1',
    '17b039a9-5643-518f-9d61-0da5ae11609f',
    'b003',
    'Generation capacity conventional thermal',
    'Capacity of electric plants using oil, gas and coal on the interconnected grid',
    'NUMBER',
    'KW, MW, GW',
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
    'c851f778-1399-5bd2-ad15-78ae9eadaa79',
    '45b2179c-6214-5b12-972f-1068923578c1',
    '17b039a9-5643-518f-9d61-0da5ae11609f',
    'b004',
    'Generation capacity nuclear',
    'Capacity of nuclear plants',
    'NUMBER',
    'KW, MW, GW',
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
    'b1efd714-b1e7-5256-8043-0fa01bc5d98a',
    '45b2179c-6214-5b12-972f-1068923578c1',
    '17b039a9-5643-518f-9d61-0da5ae11609f',
    'b005',
    'Generation capacity solar, wind, biomass, geothermal',
    'Capacity of generators using: sun, wind, wood, waste, combustible renewables and other biomass and geothermal sources.',
    'NUMBER',
    'KW, MW, GW',
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
    '9ee83bec-8f2a-5e38-9259-187af7d2cb12',
    '45b2179c-6214-5b12-972f-1068923578c1',
    '17b039a9-5643-518f-9d61-0da5ae11609f',
    'b006',
    'Generation Capacity Operational',
    'Available capacity of the power plant, i.e. the maximum capacity at which the stations can be operated (annual report)',
    'NUMBER',
    'KW, MW, GW',
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
    'bacb2ea1-20d8-58f7-9d94-66838fb1bbdd',
    '45b2179c-6214-5b12-972f-1068923578c1',
    '17b039a9-5643-518f-9d61-0da5ae11609f',
    'b007',
    'Generation capacity of isolated (off grid) systems',
    'The rated capacity as stated on the nameplate of the equipment in the isolated power plant. These are not part of the interconnected network.',
    'NUMBER',
    'KW, MW, GW',
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
    '6cb738eb-6736-5412-8c0f-58dd8ed0ab52',
    '45b2179c-6214-5b12-972f-1068923578c1',
    '17b039a9-5643-518f-9d61-0da5ae11609f',
    'b008',
    'Generation capacity of isolated (off-grid) systems in operating condition',
    'Available capacity of the isolated power plants, i.e. the maximum capacity at which the stations can be operated and it is instantly available for us',
    'NUMBER',
    'KW, MW, GW',
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
    'ab877efb-ab6e-520d-8cc5-4305a0998753',
    '45b2179c-6214-5b12-972f-1068923578c1',
    '17b039a9-5643-518f-9d61-0da5ae11609f',
    'b009',
    'Generation capacity of emergency generation',
    'Total capacity of emergency generators available per year',
    'NUMBER',
    'KW, MW, GW',
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
    '14855954-0441-593b-be4e-4b755930ebec',
    '45b2179c-6214-5b12-972f-1068923578c1',
    '17b039a9-5643-518f-9d61-0da5ae11609f',
    'b010',
    'Generation capacity of self-generation',
    'Total installed capacity of individual generators by firms',
    'NUMBER',
    'KW, MW, GW',
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
    '747a6c53-8b64-5194-aa90-f9eb91802f6d',
    '45b2179c-6214-5b12-972f-1068923578c1',
    '17b039a9-5643-518f-9d61-0da5ae11609f',
    'b012',
    'Peak demand on interconnected system',
    'Maximum load for the main interconnected network during a given year',
    'NUMBER',
    'KW, MW, GW',
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
    '5881e9de-6de9-5082-a0e6-a9da62c73702',
    '45b2179c-6214-5b12-972f-1068923578c1',
    '17b039a9-5643-518f-9d61-0da5ae11609f',
    'b013',
    'Load Served, on grid',
    'Total annual net electricity generated on the interconnected grid  per year. Total electricity generated on the interconnected grid from hydro-electric, conventional thermal, nuclear, and solar, wind, biomass, geothermal.',
    'NUMBER',
    'KWh, MWh, GWh',
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
    'f8f34dee-e72e-5e85-b1c8-3bc943294326',
    '45b2179c-6214-5b12-972f-1068923578c1',
    '17b039a9-5643-518f-9d61-0da5ae11609f',
    'b014',
    'Electricity generated on the interconnected grid from hydro-electric',
    'Consists of net electricity generated on the interconnected grid from hydro-electric plants',
    'NUMBER',
    'KWh, MWh, GWh',
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
    '5a8c6138-6220-5f9c-bebe-a9ff80e92ea8',
    '45b2179c-6214-5b12-972f-1068923578c1',
    '17b039a9-5643-518f-9d61-0da5ae11609f',
    'b015',
    'Electricity generated on the interconnected grid from conventional thermal (oil, gas, coal)',
    'Consists of electricity generated on the interconnected grid from oil, gas and coal',
    'NUMBER',
    'KWh, MWh, GWh',
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
    'aaeba50e-afdc-5483-9d9c-6ed999ae05eb',
    '45b2179c-6214-5b12-972f-1068923578c1',
    '17b039a9-5643-518f-9d61-0da5ae11609f',
    'b016',
    'Electricity generated on the interconnected grid from nuclear',
    'Consists of electricity generated on the interconnected grid from hydro-electric plants',
    'NUMBER',
    'KWh, MWh, GWh',
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
    '8eb9d728-15e7-50da-89a4-830406525511',
    '45b2179c-6214-5b12-972f-1068923578c1',
    '17b039a9-5643-518f-9d61-0da5ae11609f',
    'b017',
    'Electricity generated on the interconnected grid from solar, wind, biomass, geothermal',
    'Consists of electricity generated on the interconnected grid from solar, wind, biomass, geothermal. Includes wood, waste, combustible renewables.',
    'NUMBER',
    'KWh, MWh, GWh',
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
    '4a858f13-41e4-5174-bf99-62b934ec81f5',
    '45b2179c-6214-5b12-972f-1068923578c1',
    '17b039a9-5643-518f-9d61-0da5ae11609f',
    'b018',
    'Electricity generated by isolated (off grid) systems',
    'Total net electricity generated outside of the interconnected network by isolated (off-grid) systems',
    'NUMBER',
    'KWh, MWh, GWh',
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
    '463dd87e-cd1e-5ae7-a228-e20558b662e7',
    '45b2179c-6214-5b12-972f-1068923578c1',
    '17b039a9-5643-518f-9d61-0da5ae11609f',
    'b019',
    'Electricity generated by emergency generation',
    'Total net electricity generated by emergency generators',
    'NUMBER',
    'KWh, MWh, GWh',
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
    '61182712-149e-5585-b429-2abad2dcd172',
    '45b2179c-6214-5b12-972f-1068923578c1',
    '17b039a9-5643-518f-9d61-0da5ae11609f',
    'b020',
    'Electricity generated by self-generation',
    'Total net electricity generated by individual generators',
    'NUMBER',
    'KWh, MWh, GWh',
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
    '8f94a5bc-feef-5d8d-bdbd-9700cca14cf8',
    '45b2179c-6214-5b12-972f-1068923578c1',
    '17b039a9-5643-518f-9d61-0da5ae11609f',
    'b021',
    'Power purchased from IPPs',
    'Electricity purchased by the utility from independent power producers over the year.',
    'NUMBER',
    'KWh, MWh, GWh',
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
    'b7fc2f44-cff6-5d0d-a1b1-50a86d7e2548',
    '45b2179c-6214-5b12-972f-1068923578c1',
    '17b039a9-5643-518f-9d61-0da5ae11609f',
    'b022',
    'Imports',
    'Total Annual Net Import',
    'NUMBER',
    'KWh, MWh, GWh',
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
    '1b164b31-5609-5e6d-a00b-54129d727bfd',
    '45b2179c-6214-5b12-972f-1068923578c1',
    '17b039a9-5643-518f-9d61-0da5ae11609f',
    'b023',
    'Exports',
    'Total Annual Net Exports',
    'NUMBER',
    'KWh, MWh, GWh',
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
    '2205e4a3-f2fb-546e-b608-015e4c2b0d42',
    '45b2179c-6214-5b12-972f-1068923578c1',
    '17b039a9-5643-518f-9d61-0da5ae11609f',
    'b024',
    'HV transmission: length',
    'Total cumulative length of the high voltage transmission network',
    'NUMBER',
    'km, 10^3 km, 10^6 km',
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
    '9fb14db3-c0ab-527d-afc9-67f20cfd55a7',
    '45b2179c-6214-5b12-972f-1068923578c1',
    '17b039a9-5643-518f-9d61-0da5ae11609f',
    'b025',
    'HV transmission in need of rehabilitation: length',
    'Total cumulative length of the high voltage transmission network in need of rehabilitation',
    'NUMBER',
    'km, 10^3 km, 10^6 km',
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
    '234064ce-17fb-58f7-870c-22c3daa86603',
    '45b2179c-6214-5b12-972f-1068923578c1',
    '17b039a9-5643-518f-9d61-0da5ae11609f',
    'b026',
    'MV transmission: length',
    'Total cumulative length of the medium voltage transmission network',
    'NUMBER',
    'km, 10^3 km, 10^6 km',
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
    '48e5e83a-a667-53de-ac45-cc803055f9d6',
    '45b2179c-6214-5b12-972f-1068923578c1',
    '17b039a9-5643-518f-9d61-0da5ae11609f',
    'b027',
    'MV transmission in need of rehabilitation: length',
    'Total cumulative length of the medium voltage transmission network in need of rehabilitation',
    'NUMBER',
    'km, 10^3 km, 10^6 km',
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
    'e323d951-9fa7-5ebb-aefc-35968e3cb7c3',
    '45b2179c-6214-5b12-972f-1068923578c1',
    '17b039a9-5643-518f-9d61-0da5ae11609f',
    'b028',
    'LV transmission: length',
    'Total cumulative length of the low voltage transmission network',
    'NUMBER',
    'km, 10^3 km, 10^6 km',
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
    'ac8f7326-5f88-5a54-ad52-a7f50386c660',
    '45b2179c-6214-5b12-972f-1068923578c1',
    '17b039a9-5643-518f-9d61-0da5ae11609f',
    'b029',
    'LV transmission in need of rehabilitation: length',
    'Total cumulative length of the low voltage transmission network in need of rehabilitation',
    'NUMBER',
    'km, 10^3 km, 10^6 km',
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
    '72c38f25-3d88-5189-958f-e882eb2b6c77',
    '45b2179c-6214-5b12-972f-1068923578c1',
    '17b039a9-5643-518f-9d61-0da5ae11609f',
    'b034',
    'Load shed (GWh)',
    'Total amount of power demand that is unmet due to insufficient power, leading to blackouts ',
    'NUMBER',
    'KWh, MWh, GWh',
    TRUE,
    29,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'system',
    NULL,
    0
);

