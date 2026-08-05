-- =============================================================================
-- AIKP Platform
-- Migration : V1_001__create_schemas.sql
-- Description : Create application schemas
-- =============================================================================

CREATE SCHEMA IF NOT EXISTS iam;
COMMENT ON SCHEMA iam IS 'Identity and Access Management';

CREATE SCHEMA IF NOT EXISTS reference;
COMMENT ON SCHEMA reference IS 'Reference data';

CREATE SCHEMA IF NOT EXISTS questionnaire;
COMMENT ON SCHEMA questionnaire IS 'Questionnaire management';

CREATE SCHEMA IF NOT EXISTS campaign;
COMMENT ON SCHEMA campaign IS 'Campaign management';

CREATE SCHEMA IF NOT EXISTS datacollection;
COMMENT ON SCHEMA datacollection IS 'Data collection';

CREATE SCHEMA IF NOT EXISTS reporting;
COMMENT ON SCHEMA reporting IS 'Reporting and analytics';

CREATE SCHEMA IF NOT EXISTS audit;
COMMENT ON SCHEMA audit IS 'Audit trail';

CREATE SCHEMA IF NOT EXISTS integration;
COMMENT ON SCHEMA integration IS 'External systems integration';