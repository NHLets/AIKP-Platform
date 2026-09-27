CREATE TABLE data_collection_observation (

    id UUID PRIMARY KEY,

    data_collection_id UUID NOT NULL,

    variable_id UUID NOT NULL,

    organization_id UUID NOT NULL,

    value TEXT,

    status VARCHAR(20) NOT NULL,

    updated_at TIMESTAMP NOT NULL

);

CREATE INDEX idx_observation_collection
ON data_collection_observation(data_collection_id);

CREATE INDEX idx_observation_org
ON data_collection_observation(organization_id);

CREATE INDEX idx_observation_variable
ON data_collection_observation(variable_id);
