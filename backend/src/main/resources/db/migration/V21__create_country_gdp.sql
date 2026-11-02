-- AIKP Platform / Flyway V21 / Reference GDP (nominal, current USD)
-- Source series: World Bank WDI NY.GDP.MKTP.CD.
CREATE TABLE reference.country_gdp (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    country_id UUID NOT NULL REFERENCES reference.country(id) ON DELETE RESTRICT,
    reference_year INTEGER NOT NULL CHECK (reference_year BETWEEN 1950 AND 2100),
    gdp_current_usd NUMERIC(24,2) NOT NULL CHECK (gdp_current_usd > 0),
    source_name VARCHAR(120) NOT NULL,
    source_revision VARCHAR(40) NOT NULL,
    source_unit VARCHAR(40) NOT NULL DEFAULT 'current_USD'
        CHECK (source_unit = 'current_USD'),
    source_value NUMERIC(24,2) NOT NULL CHECK (source_value > 0),
    source_series_code VARCHAR(160) NOT NULL
        CHECK (source_series_code = 'NY.GDP.MKTP.CD'),
    source_url TEXT NOT NULL CHECK (length(trim(source_url)) > 0),
    loaded_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'migration',
    updated_at TIMESTAMPTZ,
    updated_by VARCHAR(100),
    version BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT uq_country_gdp_year UNIQUE (country_id, reference_year)
);
CREATE INDEX idx_country_gdp_year ON reference.country_gdp(reference_year);
CREATE INDEX idx_country_gdp_country_year
    ON reference.country_gdp(country_id, reference_year);
COMMENT ON TABLE reference.country_gdp IS
    'Annual nominal GDP in current US dollars; World Bank WDI series NY.GDP.MKTP.CD.';
COMMENT ON COLUMN reference.country_gdp.gdp_current_usd IS
    'GDP at current prices, expressed in current US dollars (not PPP or constant USD).';
