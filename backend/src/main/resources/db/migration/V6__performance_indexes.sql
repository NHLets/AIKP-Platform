CREATE INDEX IF NOT EXISTS idx_observation_collection
ON data_collection_observation(data_collection_id);

CREATE INDEX IF NOT EXISTS idx_observation_variable
ON data_collection_observation(variable_id);

CREATE INDEX IF NOT EXISTS idx_observation_status
ON data_collection_observation(status);

CREATE INDEX IF NOT EXISTS idx_observation_collection_variable
ON data_collection_observation(data_collection_id, variable_id);

CREATE INDEX IF NOT EXISTS idx_observation_collection_status
ON data_collection_observation(data_collection_id, status);