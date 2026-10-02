CREATE TABLE IF NOT EXISTS data_collection_observation (

    id UUID PRIMARY KEY,

    data_collection_id UUID NOT NULL,

    variable_id UUID NOT NULL,

    organization_id UUID NOT NULL,

    value TEXT,

    status VARCHAR(20) NOT NULL,

    updated_at TIMESTAMP NOT NULL

);

ALTER TABLE data_collection_observation
    ADD COLUMN IF NOT EXISTS organization_id UUID;

ALTER TABLE data_collection_observation
    ADD COLUMN IF NOT EXISTS data_collection_id UUID;

ALTER TABLE data_collection_observation
    ADD COLUMN IF NOT EXISTS variable_id UUID;

ALTER TABLE data_collection_observation
    ADD COLUMN IF NOT EXISTS value TEXT;

ALTER TABLE data_collection_observation
    ADD COLUMN IF NOT EXISTS status VARCHAR(20);

ALTER TABLE data_collection_observation
    ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;

CREATE INDEX IF NOT EXISTS idx_observation_collection
ON data_collection_observation(data_collection_id);

CREATE INDEX IF NOT EXISTS idx_observation_org
ON data_collection_observation(organization_id);

CREATE INDEX IF NOT EXISTS idx_observation_variable
ON data_collection_observation(variable_id);
