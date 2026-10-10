CREATE TABLE quality.quality_rule_evaluation (
    id UUID PRIMARY KEY,
    quality_run_id UUID NOT NULL,
    rule_code VARCHAR(100) NOT NULL,
    rule_type VARCHAR(40) NOT NULL,
    provenance VARCHAR(40) NOT NULL,
    severity VARCHAR(30) NOT NULL,
    status VARCHAR(30) NOT NULL,
    title TEXT NOT NULL,
    message TEXT NOT NULL,
    explanation TEXT,
    recommendation TEXT,
    reference_year INTEGER,
    affected_variables JSONB NOT NULL DEFAULT '[]'::jsonb,
    evidence JSONB NOT NULL DEFAULT '{}'::jsonb,

    CONSTRAINT fk_quality_rule_evaluation_run
        FOREIGN KEY (quality_run_id)
        REFERENCES quality.quality_run (id)
        ON DELETE CASCADE
);

CREATE INDEX idx_quality_rule_evaluation_run
    ON quality.quality_rule_evaluation (quality_run_id);

CREATE INDEX idx_quality_rule_evaluation_run_status
    ON quality.quality_rule_evaluation (quality_run_id, status);
