-- ============================================================================
-- AIKP Platform
-- Flyway Migration
-- Version : V2.200
-- Aggregate : Metadata / Questionnaire
-- ============================================================================

CREATE TABLE metadata.questionnaire
(
    id                   UUID          NOT NULL,
    code                 VARCHAR(50)   NOT NULL,
    name                 VARCHAR(255)  NOT NULL,
    description          VARCHAR(4000),
    questionnaire_version VARCHAR(20)  NOT NULL,
    default_language     VARCHAR(10)  NOT NULL,
    status               VARCHAR(30)   NOT NULL,
    render_type          VARCHAR(30)   NOT NULL,
    active               BOOLEAN       NOT NULL DEFAULT TRUE,

    created_at           TIMESTAMPTZ   NOT NULL,
    updated_at           TIMESTAMPTZ,
    created_by           VARCHAR(100),
    updated_by           VARCHAR(100),

    version              BIGINT        NOT NULL DEFAULT 0,

    CONSTRAINT pk_questionnaire
        PRIMARY KEY (id),

    CONSTRAINT uk_questionnaire_code
        UNIQUE (code)
);

CREATE INDEX idx_questionnaire_active
    ON metadata.questionnaire(active);

CREATE INDEX idx_questionnaire_status
    ON metadata.questionnaire(status);
