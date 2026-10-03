-- AIKP Platform
-- Allow reference year 2026 for data collection observations

ALTER TABLE reference.data_collection_observation
DROP CONSTRAINT ck_data_collection_observation_year;

ALTER TABLE reference.data_collection_observation
ADD CONSTRAINT ck_data_collection_observation_year
CHECK (reference_year BETWEEN 2015 AND 2026);
