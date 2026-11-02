#!/usr/bin/env python3
"""Validate multi-source AIKP GDP observations and generate idempotent SQL.

Input CSV columns:
iso3,year,source_value,source_scale,source_name,source_revision,
source_unit,source_series_code,source_url,source_note

The script validates the 23 active AIKP countries and 2015-2025.
It does not execute SQL. Missing observations are never imputed.
"""
import argparse
import csv
from decimal import Decimal, InvalidOperation, ROUND_HALF_UP
from pathlib import Path

YEARS = set(range(2015, 2026))

WDI_SERIES = "NY.GDP.MKTP.CD"
IMF_SERIES = "SSD.NGDPD.A"

WDI_SOURCE = "World Bank – World Development Indicators"
IMF_SOURCE = "IMF – World Economic Outlook"

WDI_SCALE = Decimal("1")
IMF_SCALE = Decimal("1000000000")

REQUIRED = {
    "iso3",
    "year",
    "source_value",
    "source_scale",
    "source_name",
    "source_revision",
    "source_unit",
    "source_series_code",
    "source_url",
    "source_note",
}

CENT = Decimal("0.01")


def quote(value: str) -> str:
    return "'" + value.replace("'", "''") + "'"


def decimal_sql(value: Decimal) -> str:
    return format(value, "f")


def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("csv_file", type=Path)
    parser.add_argument("--country-codes-file", type=Path, required=True)
    parser.add_argument("--output", type=Path, default=Path("country_gdp_seed.sql"))
    parser.add_argument("--expected-countries", type=int, default=23)
    parser.add_argument(
        "--require-complete",
        action="store_true",
        help="Fail unless all 253 country-year observations are present.",
    )
    args = parser.parse_args()

    allowed = {
        value.strip().upper()
        for value in args.country_codes_file.read_text(
            encoding="utf-8-sig"
        ).splitlines()
        if value.strip()
    }

    if len(allowed) != args.expected_countries or any(
        len(code) != 3 or not code.isalpha() for code in allowed
    ):
        raise SystemExit(
            f"Country code file must contain exactly "
            f"{args.expected_countries} unique ISO3 codes"
        )

    if "SSD" not in allowed or "ERI" not in allowed:
        raise SystemExit("The active-country list must include SSD and ERI")

    rows = {}
    seen_keys = set()

    with args.csv_file.open(newline="", encoding="utf-8-sig") as stream:
        reader = csv.DictReader(stream)
        missing_columns = REQUIRED - set(reader.fieldnames or [])

        if missing_columns:
            raise SystemExit(
                "Missing columns: " + ", ".join(sorted(missing_columns))
            )

        for line, record in enumerate(reader, start=2):
            code = record["iso3"].strip().upper()

            if code not in allowed:
                raise SystemExit(
                    f"Line {line}: ISO3 {code!r} is not in the active-country list"
                )

            try:
                year = int(record["year"])
            except ValueError:
                raise SystemExit(f"Line {line}: invalid year")

            if year not in YEARS:
                raise SystemExit(
                    f"Line {line}: year must be between 2015 and 2025"
                )

            key = (code, year)

            if key in seen_keys:
                raise SystemExit(f"Duplicate ISO3/year: {key}")

            seen_keys.add(key)

            try:
                source_value = Decimal(record["source_value"].strip())
                source_scale = Decimal(record["source_scale"].strip())
            except (InvalidOperation, AttributeError):
                raise SystemExit(f"Line {line}: invalid source value or scale")

            if not source_value.is_finite() or source_value <= 0:
                raise SystemExit(
                    f"Line {line}: source value must be finite and positive"
                )

            if not source_scale.is_finite() or source_scale <= 0:
                raise SystemExit(
                    f"Line {line}: source scale must be finite and positive"
                )

            # source_value is stored in NUMERIC(30,9).
            if max(0, -source_value.as_tuple().exponent) > 9:
                raise SystemExit(
                    f"Line {line}: source value has more than 9 decimal places"
                )

            source_name = record["source_name"].strip()
            revision = record["source_revision"].strip()
            unit = record["source_unit"].strip()
            series = record["source_series_code"].strip()
            url = record["source_url"].strip()
            source_note = record["source_note"].strip()

            if not revision or not url.startswith(("https://", "http://")):
                raise SystemExit(
                    f"Line {line}: source revision and valid source URL are required"
                )

            if unit != "USD":
                raise SystemExit(f"Line {line}: source_unit must be USD")

            if series == WDI_SERIES:
                expected_source = WDI_SOURCE
                expected_scale = WDI_SCALE

                if code == "SSD":
                    raise SystemExit(
                        f"Line {line}: SSD must use the IMF WEO series"
                    )

            elif series == IMF_SERIES:
                expected_source = IMF_SOURCE
                expected_scale = IMF_SCALE

                if code != "SSD":
                    raise SystemExit(
                        f"Line {line}: IMF WEO series is only authorized for SSD"
                    )

            else:
                raise SystemExit(
                    f"Line {line}: unsupported GDP series {series!r}"
                )

            if source_name != expected_source:
                raise SystemExit(
                    f"Line {line}: source name does not match series {series}"
                )

            if source_scale != expected_scale:
                raise SystemExit(
                    f"Line {line}: incorrect scale for series {series}; "
                    f"expected {expected_scale}"
                )

            gdp_usd = (source_value * source_scale).quantize(
                CENT, rounding=ROUND_HALF_UP
            )

            if gdp_usd <= 0:
                raise SystemExit(f"Line {line}: converted GDP must be positive")

            rows[key] = {
                "source_value": source_value,
                "source_scale": source_scale,
                "gdp_usd": gdp_usd,
                "source_name": source_name,
                "revision": revision,
                "unit": unit,
                "series": series,
                "url": url,
                "source_note": source_note,
            }

    if not rows:
        raise SystemExit("No GDP observations found in CSV")

    # Require a complete IMF WEO series for South Sudan.
    ssd_years = {
        year for code, year in rows
        if code == "SSD" and rows[(code, year)]["series"] == IMF_SERIES
    }

    if ssd_years != YEARS:
        missing_ssd = sorted(YEARS - ssd_years)
        raise SystemExit(
            "South Sudan must have all 11 IMF WEO observations. "
            f"Missing years: {missing_ssd}"
        )

    # Require complete WDI series for all countries except ERI and SSD.
    expected_wdi_countries = allowed - {"ERI", "SSD"}

    for code in sorted(expected_wdi_countries):
        observed_years = {
            year for country, year in rows
            if country == code and rows[(country, year)]["series"] == WDI_SERIES
        }

        if observed_years != YEARS:
            missing_years = sorted(YEARS - observed_years)
            raise SystemExit(
                f"{code}: expected 11 WDI observations; "
                f"missing years: {missing_years}"
            )

    # Eritrea remains missing unless separately validated in a future import.
    if any(code == "ERI" for code, _ in rows):
        raise SystemExit(
            "ERI observations are not authorized in this import: "
            "no GDP series has been confirmed."
        )

    missing = [
        (code, year)
        for code in sorted(allowed)
        for year in sorted(YEARS)
        if (code, year) not in rows
    ]

    observed_countries = {code for code, _ in rows}
    wdi_count = sum(
        1 for item in rows.values() if item["series"] == WDI_SERIES
    )
    imf_count = sum(
        1 for item in rows.values() if item["series"] == IMF_SERIES
    )

    print(f"Validated {len(rows)} GDP observations.")
    print(f"WDI observations: {wdi_count}")
    print(f"IMF WEO observations: {imf_count}")
    print(
        f"Coverage: {len(observed_countries)}/{len(allowed)} countries; "
        f"{len(rows)}/{len(allowed) * len(YEARS)} possible country-years."
    )

    if missing:
        print(
            f"Missing country-years: {len(missing)} "
            "(not inserted; no imputation)."
        )
        for code, year in missing:
            print(f"  {code} {year}")

    if args.require_complete and missing:
        raise SystemExit(
            "Complete coverage was requested; no SQL file generated."
        )

    statements = [
        "-- Generated from reviewed WDI and IMF WEO GDP data.",
        "-- Missing country-years are intentionally not inserted.",
        "BEGIN;",
    ]

    for (code, year), item in sorted(rows.items()):
        statements.append(
            "INSERT INTO reference.country_gdp "
            "(country_id, reference_year, gdp_current_usd, source_name, "
            "source_revision, source_unit, source_value, source_scale, "
            "source_series_code, source_url, source_note) "
            f"SELECT id, {year}, {decimal_sql(item['gdp_usd'])}, "
            f"{quote(item['source_name'])}, "
            f"{quote(item['revision'])}, "
            f"{quote(item['unit'])}, "
            f"{decimal_sql(item['source_value'])}, "
            f"{decimal_sql(item['source_scale'])}, "
            f"{quote(item['series'])}, "
            f"{quote(item['url'])}, "
            f"{quote(item['source_note'])} "
            f"FROM reference.country WHERE iso3_code={quote(code)} "
            "AND active=TRUE "
            "ON CONFLICT (country_id, reference_year) DO UPDATE SET "
            "gdp_current_usd=EXCLUDED.gdp_current_usd, "
            "source_name=EXCLUDED.source_name, "
            "source_revision=EXCLUDED.source_revision, "
            "source_unit=EXCLUDED.source_unit, "
            "source_value=EXCLUDED.source_value, "
            "source_scale=EXCLUDED.source_scale, "
            "source_series_code=EXCLUDED.source_series_code, "
            "source_url=EXCLUDED.source_url, "
            "source_note=EXCLUDED.source_note, "
            "loaded_at=CURRENT_TIMESTAMP, "
            "updated_at=CURRENT_TIMESTAMP;"
        )

    code_list = ", ".join(quote(code) for code in sorted(allowed))

    statements.append(
        f"""
DO $$
DECLARE actual_country_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO actual_country_count
    FROM reference.country
    WHERE active=TRUE AND iso3_code IN ({code_list});

    IF actual_country_count <> {args.expected_countries} THEN
        RAISE EXCEPTION 'Expected {args.expected_countries} active AIKP countries; found %',
            actual_country_count;
    END IF;
END $$;
""".strip()
    )

    statements.append("COMMIT;")

    args.output.write_text(
        "\n".join(statements) + "\n",
        encoding="utf-8",
    )

    print(f"SQL written to {args.output}; no SQL was executed.")


if __name__ == "__main__":
    main()
