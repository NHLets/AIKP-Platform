-- ============================================================================
-- AIKP Platform
-- Flyway Migration
-- Version : V2.983
-- Aggregate : Metadata / Questionnaire / PW_A
-- ============================================================================

INSERT INTO metadata.questionnaire
(
    id, code, name, description, questionnaire_version, default_language,
    status, render_type, active, created_at, updated_at,
    created_by, updated_by, version
)
VALUES
(
    '01222ce5-e554-42ea-a537-7b65b2724198',
    'PW_A',
    'Power Data Template A: National Level Institutions',
    'National level institutions power sector policy questionnaire based on Power Data Template A.',
    '1.0',
    'en',
    'DRAFT',
    'SPREADSHEET',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL,
    'SYSTEM',
    NULL,
    0
)
ON CONFLICT (id) DO NOTHING;

INSERT INTO metadata.questionnaire_group
(
    id, questionnaire_id, parent_group_id, code, name, description,
    group_type, display_order, active, created_at, updated_at,
    created_by, updated_by, version
)
VALUES
('81148593-246e-45f2-bb5a-33a2f8572a02', '01222ce5-e554-42ea-a537-7b65b2724198', NULL,
 'RESTRUCTURING', 'Restructuring', 'Power sector restructuring policy indicators.',
 'POLICY_GROUP', 1, TRUE, CURRENT_TIMESTAMP, NULL, 'SYSTEM', NULL, 0),
('6a40916b-cf18-4f34-9755-9af62054b9aa', '01222ce5-e554-42ea-a537-7b65b2724198', NULL,
 'DECENTRALIZATION', 'Decentralization', 'Power sector decentralization policy indicators.',
 'POLICY_GROUP', 2, TRUE, CURRENT_TIMESTAMP, NULL, 'SYSTEM', NULL, 0),
('bbca4e5a-6312-4337-aacd-1a97e8f45fef', '01222ce5-e554-42ea-a537-7b65b2724198', NULL,
 'MARKET_STRUCTURE', 'Market Structure', 'Power sector market structure indicators.',
 'POLICY_GROUP', 3, TRUE, CURRENT_TIMESTAMP, NULL, 'SYSTEM', NULL, 0),
('9e66822f-c215-4ee8-917a-502b82637c25', '01222ce5-e554-42ea-a537-7b65b2724198', NULL,
 'REGULATION_TOOLS', 'Regulation Tools', 'Power sector regulatory tools and requirements.',
 'POLICY_GROUP', 4, TRUE, CURRENT_TIMESTAMP, NULL, 'SYSTEM', NULL, 0),
('89db1670-7b57-4693-a5b0-b291eef4f892', '01222ce5-e554-42ea-a537-7b65b2724198', NULL,
 'COST_RECOVERY', 'Cost Recovery', 'Power sector cost recovery policy indicators.',
 'POLICY_GROUP', 5, TRUE, CURRENT_TIMESTAMP, NULL, 'SYSTEM', NULL, 0),
('809d2a6e-9492-453d-97c7-7f2bf2c31a87', '01222ce5-e554-42ea-a537-7b65b2724198', NULL,
 'ENVIRONMENTAL', 'Environmental', 'Power sector environmental and renewable energy policy indicators.',
 'POLICY_GROUP', 6, TRUE, CURRENT_TIMESTAMP, NULL, 'SYSTEM', NULL, 0)
ON CONFLICT (id) DO NOTHING;

INSERT INTO metadata.questionnaire_variable
(
    id, questionnaire_id, questionnaire_group_id, series_code, name, definition,
    data_type, unit, required, display_order, active, created_at, updated_at,
    created_by, updated_by, version
)
VALUES
('de1d545b-415c-4750-98b9-410f63a0ca87', '01222ce5-e554-42ea-a537-7b65b2724198', '81148593-246e-45f2-bb5a-33a2f8572a02', 'D001', 'Sector Specific: De Jure Unbundling Generation-Transmission', 'By Law, companies providing generation and transmission of electricity cannot be owned by the same operator. Coding: 1=yes, 0=no.', 'BOOLEAN', NULL, TRUE, 1, TRUE, CURRENT_TIMESTAMP, NULL, 'SYSTEM', NULL, 0),
('b3981ff5-5bcc-497a-89f5-6df0a4976478', '01222ce5-e554-42ea-a537-7b65b2724198', '81148593-246e-45f2-bb5a-33a2f8572a02', 'D002', 'Sector Specific: De facto Unbundling Generation-Transmission', 'Generation of electricity and Transmission of Electricity are provided by different companies. Coding: 1=yes, 0=no.', 'BOOLEAN', NULL, TRUE, 2, TRUE, CURRENT_TIMESTAMP, NULL, 'SYSTEM', NULL, 0),
('38cc7293-fe86-4968-87d9-0d16c7e970e2', '01222ce5-e554-42ea-a537-7b65b2724198', '81148593-246e-45f2-bb5a-33a2f8572a02', 'D003', 'Sector Specific: De Jure Unbundling Distribution-Transmission', 'By Law, companies providing distribution and transmission of electricity cannot be owned by the same operator. Coding: 1=yes, 0=no.', 'BOOLEAN', NULL, TRUE, 3, TRUE, CURRENT_TIMESTAMP, NULL, 'SYSTEM', NULL, 0),
('fc5c505d-5964-44f1-8d4c-d9d995e2a6c8', '01222ce5-e554-42ea-a537-7b65b2724198', '81148593-246e-45f2-bb5a-33a2f8572a02', 'D004', 'Sector Specific: De Facto Unbundling Distribution-Transmission', 'Distribution of electricity and Transmission of Electricity are provided by different companies. Coding: 1=yes, 0=no.', 'BOOLEAN', NULL, TRUE, 4, TRUE, CURRENT_TIMESTAMP, NULL, 'SYSTEM', NULL, 0),
('cc29eb04-fa00-486b-bfc4-f4c28ec76fb4', '01222ce5-e554-42ea-a537-7b65b2724198', '81148593-246e-45f2-bb5a-33a2f8572a02', 'D005', 'Sector Specific: De Jure Unbundling Generation-Distribution', 'By Law, companies providing generation and distribution of electricity cannot be owned by the same operator. Coding: 1=yes, 0=no.', 'BOOLEAN', NULL, TRUE, 5, TRUE, CURRENT_TIMESTAMP, NULL, 'SYSTEM', NULL, 0),
('34bbcaee-3995-4ff4-b4d8-6c6b09c1a543', '01222ce5-e554-42ea-a537-7b65b2724198', '81148593-246e-45f2-bb5a-33a2f8572a02', 'D006', 'Sector Specific: De facto Unbundling Generation-Distribution', 'Generation of electricity and Distribution of Electricity are provided by different companies. Coding: 1=yes, 0=no.', 'BOOLEAN', NULL, TRUE, 6, TRUE, CURRENT_TIMESTAMP, NULL, 'SYSTEM', NULL, 0),
('9762328f-79b7-482e-b760-e5f97b951b82', '01222ce5-e554-42ea-a537-7b65b2724198', '6a40916b-cf18-4f34-9755-9af62054b9aa', 'D007', 'Sector Specific: Jurisdiction for Rural Electrification Provision', 'Level of government responsible for rural electrification. Coding: 0=Central, 1=Regional, 2=Local/Municipal.', 'INTEGER', NULL, TRUE, 1, TRUE, CURRENT_TIMESTAMP, NULL, 'SYSTEM', NULL, 0),
('1c5323d8-a758-465f-969b-072bbfbf9072', '01222ce5-e554-42ea-a537-7b65b2724198', '6a40916b-cf18-4f34-9755-9af62054b9aa', 'D011', 'Sector Specific: Urban Utility Decentralization', 'National urban utility does not have any significant responsibilities in states and municipalities. Coding: 1=yes, 0=no.', 'BOOLEAN', NULL, TRUE, 2, TRUE, CURRENT_TIMESTAMP, NULL, 'SYSTEM', NULL, 0),
('4cf73ce5-8cf6-4dcb-adde-8d8f13b4c9d6', '01222ce5-e554-42ea-a537-7b65b2724198', 'bbca4e5a-6312-4337-aacd-1a97e8f45fef', 'D012', 'Reform: Market Model', 'Description of the market structure based on the level of competition within each segment of the industry and the level of competition allowed. Coding: 0=same company, 1=single buyer model, 2=Wholesale competition, 3=Retail competition.', 'INTEGER', NULL, TRUE, 1, TRUE, CURRENT_TIMESTAMP, NULL, 'SYSTEM', NULL, 0),
('00700ae7-79f3-424b-827b-0e27aa5c247e', '01222ce5-e554-42ea-a537-7b65b2724198', 'bbca4e5a-6312-4337-aacd-1a97e8f45fef', 'D017', 'Reform: Number of Operators Generation', 'Number of active operators currently providing the service.', 'INTEGER', 'Number', TRUE, 2, TRUE, CURRENT_TIMESTAMP, NULL, 'SYSTEM', NULL, 0),
('d22aa3a2-a348-418a-8581-3e6e64db74f5', '01222ce5-e554-42ea-a537-7b65b2724198', 'bbca4e5a-6312-4337-aacd-1a97e8f45fef', 'D021', 'Reform: Number of Operators Transmission', 'Number of active operators currently providing the service.', 'INTEGER', 'Number', TRUE, 3, TRUE, CURRENT_TIMESTAMP, NULL, 'SYSTEM', NULL, 0),
('014392ea-14bb-43d8-856c-b02ceec678c5', '01222ce5-e554-42ea-a537-7b65b2724198', 'bbca4e5a-6312-4337-aacd-1a97e8f45fef', 'D025', 'Reform: Number of Operators Distribution', 'Number of active operators currently providing the service.', 'INTEGER', 'Number', TRUE, 4, TRUE, CURRENT_TIMESTAMP, NULL, 'SYSTEM', NULL, 0),
('4f152da7-f22b-4e04-b79d-1f03ce88537b', '01222ce5-e554-42ea-a537-7b65b2724198', 'bbca4e5a-6312-4337-aacd-1a97e8f45fef', 'D029', 'Sector Specific: Community Providers', 'Community based service providers have some significant responsibilities in provision of rural power. Coding: 1=yes, 0=no.', 'BOOLEAN', NULL, TRUE, 5, TRUE, CURRENT_TIMESTAMP, NULL, 'SYSTEM', NULL, 0),
('a90225ad-143c-4081-95e0-37e2a6048c18', '01222ce5-e554-42ea-a537-7b65b2724198', '9e66822f-c215-4ee8-917a-502b82637c25', 'D030', 'Sector Specific: Regulation Large Customers', 'Whether large customers are regulated. Coding: 1=yes, 0=no.', 'BOOLEAN', NULL, TRUE, 1, TRUE, CURRENT_TIMESTAMP, NULL, 'SYSTEM', NULL, 0),
('5bba60b7-7f91-48fd-ac47-a826c95e653b', '01222ce5-e554-42ea-a537-7b65b2724198', '9e66822f-c215-4ee8-917a-502b82637c25', 'D031', 'Sector Specific: Transmission Tariff', 'Tariff regulation methodology used for transmission. Coding: 0=none, 1=price cap, 2=rate of return, 3=other.', 'INTEGER', NULL, TRUE, 2, TRUE, CURRENT_TIMESTAMP, NULL, 'SYSTEM', NULL, 0),
('2c823fac-7195-4707-a10c-d68f33b9b51e', '01222ce5-e554-42ea-a537-7b65b2724198', '9e66822f-c215-4ee8-917a-502b82637c25', 'D036', 'Sector Specific: Third Party Access', 'Whether Third Party Access to transmisison and distribution network is allowed by Law. Coding: 1=yes, 0=no.', 'BOOLEAN', NULL, TRUE, 3, TRUE, CURRENT_TIMESTAMP, NULL, 'SYSTEM', NULL, 0),
('7da36477-9692-465c-bcb3-e96bdc0b8bf2', '01222ce5-e554-42ea-a537-7b65b2724198', '9e66822f-c215-4ee8-917a-502b82637c25', 'D037', 'Sector Specific: Minimum quality standards', 'Whether regulation defines minimum quality standards for operatios. Coding: 1=yes, 0=no.', 'BOOLEAN', NULL, TRUE, 4, TRUE, CURRENT_TIMESTAMP, NULL, 'SYSTEM', NULL, 0),
('224271e2-992b-4779-b2a4-4f9e9e6e0ee4', '01222ce5-e554-42ea-a537-7b65b2724198', '9e66822f-c215-4ee8-917a-502b82637c25', 'D038', 'Sector Specific: Penalties for non Compliance', 'Whether regulation establishes penalties for non compliance to minimum quality standards. Coding: 1=yes, 0=no.', 'BOOLEAN', NULL, TRUE, 5, TRUE, CURRENT_TIMESTAMP, NULL, 'SYSTEM', NULL, 0),
('9d8dfc1d-4eed-4713-9cde-16baa42ea4d0', '01222ce5-e554-42ea-a537-7b65b2724198', '9e66822f-c215-4ee8-917a-502b82637c25', 'D039', 'Sector Specific: Cut off possibility', 'Whether utility can cut-off service in case of non-paryment. Coding: 1=yes, 0=no.', 'BOOLEAN', NULL, TRUE, 6, TRUE, CURRENT_TIMESTAMP, NULL, 'SYSTEM', NULL, 0),
('0f425474-9b20-40c5-9298-d36c8819f3b7', '01222ce5-e554-42ea-a537-7b65b2724198', '89db1670-7b57-4693-a5b0-b291eef4f892', 'D040', 'Sector Specific: Cost recovery of Rural Fund', 'Policy on cost recovery for electricity services in rural electricity services. Coding: 0=full subsidy, 1=full capital subsidy, 2=partial capital subisidy, 3=no subsidy.', 'INTEGER', NULL, TRUE, 1, TRUE, CURRENT_TIMESTAMP, NULL, 'SYSTEM', NULL, 0),
('6f20a42f-18bc-4cca-9c40-3beb7e75dd01', '01222ce5-e554-42ea-a537-7b65b2724198', '809d2a6e-9492-453d-97c7-7f2bf2c31a87', 'D044', 'Sector Specific: Renewable energy', 'Whether there are incentives for renewable energy. Coding: 1=yes, 0=no.', 'BOOLEAN', NULL, TRUE, 1, TRUE, CURRENT_TIMESTAMP, NULL, 'SYSTEM', NULL, 0)
ON CONFLICT (id) DO NOTHING;
