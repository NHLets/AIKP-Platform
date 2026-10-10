-- AIKP Platform
-- Allow rule evaluations without a message, e.g. PASSED evaluations.

ALTER TABLE quality.quality_rule_evaluation
ALTER COLUMN message DROP NOT NULL;
