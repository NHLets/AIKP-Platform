-- V22: Support multiple GDP sources with explicit source scaling.
-- WDI: source values in USD, scale = 1.
-- IMF WEO: source values in billions USD, scale = 1,000,000,000.

ALTER TABLE reference.country_gdp
    DROP CONSTRAINT country_gdp_source_series_code_check,
    DROP CONSTRAINT country_gdp_source_unit_check;

ALTER TABLE reference.country_gdp
    ALTER COLUMN source_value TYPE NUMERIC(30,9)
    USING source_value::NUMERIC(30,9);

ALTER TABLE reference.country_gdp
    ADD COLUMN source_scale NUMERIC(20,0) NOT NULL DEFAULT 1,
    ADD COLUMN source_note TEXT;

ALTER TABLE reference.country_gdp
    ALTER COLUMN source_unit SET DEFAULT 'USD';

UPDATE reference.country_gdp
SET source_unit = 'USD';

ALTER TABLE reference.country_gdp
    ADD CONSTRAINT country_gdp_source_series_code_check
        CHECK (source_series_code IN ('NY.GDP.MKTP.CD', 'SSD.NGDPD.A')),

    ADD CONSTRAINT country_gdp_source_unit_check
        CHECK (source_unit = 'USD'),

    ADD CONSTRAINT country_gdp_source_scale_check
        CHECK (
            (source_series_code = 'NY.GDP.MKTP.CD' AND source_scale = 1)
            OR
            (source_series_code = 'SSD.NGDPD.A' AND source_scale = 1000000000)
        ),

    ADD CONSTRAINT country_gdp_source_conversion_check
        CHECK (gdp_current_usd = ROUND(source_value * source_scale, 2));
