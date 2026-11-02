#!/usr/bin/env python3
"""Build the reviewed AIKP GDP CSV from World Bank WDI and IMF WEO sources."""

import csv
import json
from decimal import Decimal, InvalidOperation
from pathlib import Path

from openpyxl import load_workbook

YEARS = list(range(2015, 2026))

COUNTRIES = {
    "BDI", "BFA", "CAF", "CMR", "COD", "COM", "DJI",
    "ERI", "GIN", "GMB", "GNB", "LBR", "MDG", "MLI",
    "MOZ", "NER", "SDN", "SLE", "SOM", "SSD", "STP",
    "TCD", "TGO",
}

WDI_FILE = Path("/tmp/aikp_wdi_gdp_api.json")
IMF_FILE = Path("/tmp/WEOOct2026all.xlsx")
OUTPUT = Path("/tmp/aikp_country_gdp_2015_2025.csv")

WDI_SERIES = "NY.GDP.MKTP.CD"
IMF_SERIES = "SSD.NGDPD.A"

WDI_URL = (
    "https://api.worldbank.org/v2/country/"
    + ";".join(sorted(COUNTRIES))
    + "/indicator/NY.GDP.MKTP.CD"
    "?date=2015:2025&format=json&per_page=1000"
)

IMF_URL = (
    "https://data.imf.org/-/media/iData/External-Storage/Documents/"
    "0FA3DC49A04D446E9D69DBEB29979E1C/en/WEOOct2026all.xlsx"
)

FIELDS = [
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
]


def fail(message):
    raise SystemExit("VALIDATION ERROR: " + message)


def decimal_value(value, label):
    if value is None:
        fail(f"{label}: missing value")
    try:
        result = Decimal(str(value))
    except (InvalidOperation, ValueError):
        fail(f"{label}: invalid numeric value {value!r}")
    if not result.is_finite() or result <= 0:
        fail(f"{label}: value must be finite and positive")
    return result


def decimal_text(value):
    return format(value, "f")


def build_wdi():
    if not WDI_FILE.is_file():
        fail(f"WDI file not found: {WDI_FILE}")

    with WDI_FILE.open(encoding="utf-8") as stream:
        payload = json.load(stream, parse_float=Decimal)

    if not isinstance(payload, list) or len(payload) != 2:
        fail("Unexpected World Bank API JSON structure")

    metadata, observations = payload

    if metadata.get("lastupdated") != "2026-10-08":
        fail(
            "Unexpected WDI revision: "
            f"{metadata.get('lastupdated')!r}"
        )

    if metadata.get("total") != 253:
        fail(f"Expected 253 WDI records; found {metadata.get('total')}")

    if len(observations) != 253:
        fail(f"Expected 253 returned WDI records; found {len(observations)}")

    seen = set()
    values = {}

    for record in observations:
        code = record.get("countryiso3code")
        year = int(record["date"])

        if code not in COUNTRIES:
            fail(f"Unexpected WDI country: {code}")

        if year not in YEARS:
            fail(f"Unexpected WDI year: {year}")

        if record.get("indicator", {}).get("id") != WDI_SERIES:
            fail(f"Unexpected WDI indicator for {code} {year}")

        key = (code, year)
        if key in seen:
            fail(f"Duplicate WDI observation: {key}")
        seen.add(key)

        value = record.get("value")
        values[key] = (
            None if value is None
            else decimal_value(value, f"WDI {code} {year}")
        )

    expected_keys = {
        (code, year)
        for code in COUNTRIES
        for year in YEARS
    }

    if seen != expected_keys:
        fail(
            "WDI country/year coverage differs from the expected "
            "23 countries x 11 years"
        )

    wdi_countries = COUNTRIES - {"ERI", "SSD"}
    rows = []

    for code in sorted(wdi_countries):
        for year in YEARS:
            value = values[(code, year)]
            if value is None:
                fail(f"Missing WDI value for {code} {year}")

            rows.append({
                "iso3": code,
                "year": year,
                "source_value": decimal_text(value),
                "source_scale": "1",
                "source_name": "World Bank – World Development Indicators",
                "source_revision": "2026-10-08",
                "source_unit": "USD",
                "source_series_code": WDI_SERIES,
                "source_url": WDI_URL,
                "source_note": (
                    "World Bank WDI API; metadata lastupdated=2026-10-08."
                ),
            })

    # SSD is deliberately sourced entirely from IMF WEO.
    ssd_wdi_values = {
        year: values[("SSD", year)]
        for year in YEARS
    }

    nonmissing_ssd = {
        year: value
        for year, value in ssd_wdi_values.items()
        if value is not None
    }

    if set(nonmissing_ssd) != {2015}:
        fail(
            "Expected only the 2015 WDI SSD observation to be nonmissing; "
            f"found years {sorted(nonmissing_ssd)}"
        )

    # Eritrea must remain without observations.
    if any(values[("ERI", year)] is not None for year in YEARS):
        fail("Unexpected nonmissing WDI observation for Eritrea")

    return rows


def build_imf():
    if not IMF_FILE.is_file():
        fail(f"IMF workbook not found: {IMF_FILE}")

    workbook = load_workbook(
        IMF_FILE,
        read_only=True,
        data_only=True,
    )

    try:
        if "Countries" not in workbook.sheetnames:
            fail("IMF workbook does not contain the Countries sheet")

        sheet = workbook["Countries"]
        iterator = sheet.iter_rows(values_only=True)

        headers = next(iterator)
        indexes = {
            str(value).strip(): index
            for index, value in enumerate(headers)
            if value is not None
        }

        required_headers = {
            "SERIES_CODE",
            "COUNTRY.ID",
            "COUNTRY",
            "INDICATOR.ID",
            "FREQUENCY",
            "SCALE",
            "UNIT",
            "METHODOLOGY_NOTES",
            "LATEST_ACTUAL_ANNUAL_DATA",
            "HISTORICAL_DATA_SOURCE",
        }

        missing = required_headers - set(indexes)
        if missing:
            fail(f"Missing IMF columns: {sorted(missing)}")

        for year in YEARS:
            if str(year) not in indexes:
                fail(f"IMF year column {year} not found")

        matching_rows = []

        for row in iterator:
            if row[indexes["SERIES_CODE"]] == IMF_SERIES:
                matching_rows.append(row)

        if len(matching_rows) != 1:
            fail(
                f"Expected exactly one IMF series {IMF_SERIES}; "
                f"found {len(matching_rows)}"
            )

        row = matching_rows[0]

        def field(name):
            return row[indexes[name]]

        checks = {
            "COUNTRY.ID": "SSD",
            "COUNTRY": "South Sudan, Republic of",
            "INDICATOR.ID": "NGDPD",
            "FREQUENCY": "Annual",
            "SCALE": "Billions",
            "UNIT": "US dollar",
            "LATEST_ACTUAL_ANNUAL_DATA": 2025,
            "HISTORICAL_DATA_SOURCE": "IMF Staff Estimates",
        }

        for name, expected in checks.items():
            actual = field(name)
            if str(actual).strip() != str(expected):
                fail(
                    f"Unexpected IMF {name}: {actual!r}; "
                    f"expected {expected!r}"
                )

        methodology = str(field("METHODOLOGY_NOTES") or "").strip()

        if "Preliminary results by NBS" not in methodology:
            fail("IMF preliminary NBS methodology note is missing")

        if "July/June" not in methodology:
            fail("IMF July/June fiscal-year note is missing")

        dataset = str(field("DATASET")).strip()

        rows = []

        for year in YEARS:
            value = decimal_value(
                row[indexes[str(year)]],
                f"IMF SSD {year}",
            )

            rows.append({
                "iso3": "SSD",
                "year": year,
                "source_value": decimal_text(value),
                "source_scale": "1000000000",
                "source_name": "IMF – World Economic Outlook",
                "source_revision": "October 2026",
                "source_unit": "USD",
                "source_series_code": IMF_SERIES,
                "source_url": IMF_URL,
                "source_note": (
                    f"Dataset: {dataset}. "
                    f"Historical data source: {field('HISTORICAL_DATA_SOURCE')}. "
                    f"Methodology: {methodology}"
                ),
            })

        return rows

    finally:
        workbook.close()


def main():
    wdi_rows = build_wdi()
    imf_rows = build_imf()

    rows = wdi_rows + imf_rows

    keys = [(row["iso3"], row["year"]) for row in rows]

    if len(keys) != len(set(keys)):
        fail("Duplicate country/year in final CSV")

    if len(wdi_rows) != 231:
        fail(f"Expected 231 WDI rows; found {len(wdi_rows)}")

    if len(imf_rows) != 11:
        fail(f"Expected 11 IMF rows; found {len(imf_rows)}")

    if len(rows) != 242:
        fail(f"Expected 242 total rows; found {len(rows)}")

    missing = [
        (code, year)
        for code in sorted(COUNTRIES)
        for year in YEARS
        if (code, year) not in set(keys)
    ]

    expected_missing = {
        ("ERI", year) for year in YEARS
    }

    if set(missing) != expected_missing:
        fail(f"Unexpected missing country-years: {missing}")

    OUTPUT.parent.mkdir(parents=True, exist_ok=True)

    with OUTPUT.open("w", newline="", encoding="utf-8") as stream:
        writer = csv.DictWriter(stream, fieldnames=FIELDS)
        writer.writeheader()
        writer.writerows(rows)

    print("CSV successfully built.")
    print(f"Output: {OUTPUT}")
    print(f"WDI observations: {len(wdi_rows)}")
    print(f"IMF WEO observations: {len(imf_rows)}")
    print(f"Total observations: {len(rows)}")
    print(f"Missing observations: {len(missing)} (ERI, 2015–2025)")
    print("No interpolation or imputation performed.")


if __name__ == "__main__":
    main()
