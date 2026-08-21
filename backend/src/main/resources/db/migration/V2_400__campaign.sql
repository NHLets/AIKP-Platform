-- ============================================================================
-- AIKP Platform
-- Flyway Migration
-- Version : V2.400
-- Aggregate : Campaign
-- Purpose : Create the Campaign aggregate persistence table
-- ============================================================================

CREATE SCHEMA IF NOT EXISTS campaign;

COMMENT ON SCHEMA campaign
IS 'Campaign management and data collection lifecycle';

CREATE TABLE campaign.campaign
(
    id             UUID          NOT NULL,
    code           VARCHAR(50)   NOT NULL,
    name           VARCHAR(255)  NOT NULL,
    description    VARCHAR(1000) NOT NULL,

    start_date     DATE          NOT NULL,
    end_date       DATE          NOT NULL,

    status         VARCHAR(30)   NOT NULL,

    created_at     TIMESTAMPTZ   NOT NULL,
    updated_at     TIMESTAMPTZ,
    created_by     VARCHAR(100),
    updated_by     VARCHAR(100),

    version        BIGINT        NOT NULL DEFAULT 0,

    CONSTRAINT pk_campaign
        PRIMARY KEY (id),

    CONSTRAINT uk_campaign_code
        UNIQUE (code),

    CONSTRAINT ck_campaign_period
        CHECK (end_date >= start_date)
);

CREATE INDEX idx_campaign_status
    ON campaign.campaign(status);
