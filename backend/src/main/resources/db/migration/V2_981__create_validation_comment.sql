SET search_path = reference;

CREATE TABLE validation_comment (

    id UUID PRIMARY KEY,

    observation_id UUID NOT NULL,

    validator_id UUID NOT NULL,

    comment TEXT NOT NULL,

    severity VARCHAR(20) NOT NULL,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_validation_comment_observation
        FOREIGN KEY (observation_id)
        REFERENCES reference.data_collection_observation(id)
        ON DELETE CASCADE

);

CREATE INDEX idx_validation_comment_observation
ON validation_comment(observation_id);

CREATE INDEX idx_validation_comment_validator
ON validation_comment(validator_id);

CREATE INDEX idx_validation_comment_created_at
ON validation_comment(created_at DESC);
