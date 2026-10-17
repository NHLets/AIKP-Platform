-- Preserve historical person-based validation rows while recording IAM users for new decisions.
ALTER TABLE reference.data_collection_validation
    ALTER COLUMN validator_id DROP NOT NULL;

ALTER TABLE reference.data_collection_validation
    ADD COLUMN validator_user_id UUID NULL;

ALTER TABLE reference.data_collection_validation
    ADD CONSTRAINT fk_data_collection_validation_validator_user
    FOREIGN KEY (validator_user_id) REFERENCES identity.users(id) ON DELETE RESTRICT;

ALTER TABLE reference.data_collection_validation
    ADD CONSTRAINT chk_data_collection_validation_validator_present
    CHECK (validator_id IS NOT NULL OR validator_user_id IS NOT NULL);

CREATE INDEX idx_data_collection_validation_validator_user_id
    ON reference.data_collection_validation(validator_user_id);
