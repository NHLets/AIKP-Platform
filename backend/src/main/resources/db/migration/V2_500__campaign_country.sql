CREATE TABLE campaign.campaign_country (
    id UUID NOT NULL,
    campaign_id UUID NOT NULL,
    country_id UUID NOT NULL,

    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    created_by VARCHAR(255),

    updated_at TIMESTAMP WITH TIME ZONE,
    updated_by VARCHAR(255),

    version BIGINT NOT NULL DEFAULT 0,

    CONSTRAINT pk_campaign_country
        PRIMARY KEY (id),

    CONSTRAINT uq_campaign_country_campaign_country
        UNIQUE (campaign_id, country_id),

    CONSTRAINT fk_campaign_country_campaign
        FOREIGN KEY (campaign_id)
        REFERENCES campaign.campaign (id)
        ON DELETE CASCADE,

    CONSTRAINT fk_campaign_country_country
        FOREIGN KEY (country_id)
        REFERENCES reference.country (id)
        ON DELETE RESTRICT
);

CREATE INDEX idx_campaign_country_campaign_id
    ON campaign.campaign_country (campaign_id);

CREATE INDEX idx_campaign_country_country_id
    ON campaign.campaign_country (country_id);
