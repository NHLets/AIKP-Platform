CREATE SCHEMA IF NOT EXISTS quality;

CREATE TABLE IF NOT EXISTS quality.quality_run (
    id UUID PRIMARY KEY,
    data_collection_id UUID NOT NULL,
    trigger VARCHAR(30) NOT NULL,
    status VARCHAR(30) NOT NULL,
    started_at TIMESTAMPTZ NOT NULL,
    completed_at TIMESTAMPTZ,
    rules_evaluated INTEGER NOT NULL DEFAULT 0,
    rules_passed INTEGER NOT NULL DEFAULT 0,
    rules_failed INTEGER NOT NULL DEFAULT 0,
    rules_not_evaluable INTEGER NOT NULL DEFAULT 0,
    rules_not_applicable INTEGER NOT NULL DEFAULT 0,
    rules_errored INTEGER NOT NULL DEFAULT 0
);

CREATE INDEX IF NOT EXISTS idx_quality_run_data_collection
    ON quality.quality_run(data_collection_id);

CREATE INDEX IF NOT EXISTS idx_quality_run_latest
    ON quality.quality_run(data_collection_id, started_at DESC);

CREATE TABLE IF NOT EXISTS quality.quality_finding (
    id UUID PRIMARY KEY,
    quality_run_id UUID NOT NULL,
    rule_code VARCHAR(100) NOT NULL,
    rule_type VARCHAR(50) NOT NULL,
    provenance VARCHAR(30) NOT NULL,
    severity VARCHAR(20) NOT NULL,
    status VARCHAR(30) NOT NULL,
    title VARCHAR(500) NOT NULL,
    message TEXT,
    explanation TEXT,
    recommendation TEXT,
    reference_year INTEGER,
    affected_variables JSONB,
    evidence JSONB,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_quality_finding_run
        FOREIGN KEY (quality_run_id)
        REFERENCES quality.quality_run(id)
        ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_quality_finding_run
    ON quality.quality_finding(quality_run_id);

CREATE INDEX IF NOT EXISTS idx_quality_finding_rule
    ON quality.quality_finding(rule_code);

CREATE INDEX IF NOT EXISTS idx_quality_finding_severity
    ON quality.quality_finding(severity);
