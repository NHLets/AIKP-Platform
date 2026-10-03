#!/usr/bin/env python3

from __future__ import annotations

import subprocess
import sys
from textwrap import dedent


CAMPAIGN_ID = "e834a2a1-52a8-4d89-8795-f94b4f699a04"
QUESTIONNAIRE_ID = "45b2179c-6214-5b12-972f-1068923578c1"

COUNTRY_ID = "11111111-2026-4000-8000-000000000001"
ORGANIZATION_ID = "11111111-2026-4000-8000-000000000002"
PERSON_ID = "11111111-2026-4000-8000-000000000003"
DATA_COLLECTION_ID = "11111111-2026-4000-8000-000000000004"

OBSERVATION_B001_ID = "11111111-2026-4000-8000-000000000005"
OBSERVATION_B002_ID = "11111111-2026-4000-8000-000000000006"

COMMENT_1_ID = "11111111-2026-4000-8000-000000000007"
COMMENT_2_ID = "11111111-2026-4000-8000-000000000008"
COMMENT_3_ID = "11111111-2026-4000-8000-000000000009"

VALIDATION_1_ID = "11111111-2026-4000-8000-000000000010"
VALIDATION_2_ID = "11111111-2026-4000-8000-000000000011"
VALIDATION_3_ID = "11111111-2026-4000-8000-000000000012"

COUNTRY_ISO2 = "ZX"
COUNTRY_ISO3 = "ZXX"
COUNTRY_NUMERIC = "926"
ORGANIZATION_CODE = "AN-2026-TEST"


def main() -> int:
    sql = dedent(f"""
        BEGIN;

        DELETE FROM reference.validation_comment
        WHERE id IN (
            '{COMMENT_1_ID}', '{COMMENT_2_ID}', '{COMMENT_3_ID}'
        );

        DELETE FROM reference.data_collection_validation
        WHERE id IN (
            '{VALIDATION_1_ID}', '{VALIDATION_2_ID}', '{VALIDATION_3_ID}'
        );

        DELETE FROM reference.data_collection_observation
        WHERE id IN (
            '{OBSERVATION_B001_ID}', '{OBSERVATION_B002_ID}'
        );

        DELETE FROM reference.data_collection
        WHERE id = '{DATA_COLLECTION_ID}';

        DELETE FROM reference.person
        WHERE id = '{PERSON_ID}';

        DELETE FROM reference.organization
        WHERE id = '{ORGANIZATION_ID}'
           OR code = '{ORGANIZATION_CODE}';

        DELETE FROM reference.country
        WHERE id = '{COUNTRY_ID}'
           OR iso2_code = '{COUNTRY_ISO2}'
           OR iso3_code = '{COUNTRY_ISO3}'
           OR numeric_code = '{COUNTRY_NUMERIC}';

        INSERT INTO reference.country (
            id, iso2_code, iso3_code, numeric_code,
            name, official_name, created_at
        )
        VALUES (
            '{COUNTRY_ID}', '{COUNTRY_ISO2}', '{COUNTRY_ISO3}',
            '{COUNTRY_NUMERIC}',
            'Analytics Test Country 2026',
            'Analytics Test Country 2026',
            CURRENT_TIMESTAMP
        );

        INSERT INTO reference.organization (
            id, code, name, type, country_id, created_at
        )
        VALUES (
            '{ORGANIZATION_ID}',
            '{ORGANIZATION_CODE}',
            'Analytics Test Organization 2026',
            'GOVERNMENT_AGENCY',
            '{COUNTRY_ID}',
            CURRENT_TIMESTAMP
        );

        INSERT INTO reference.person (
            id, full_name, organization_id
        )
        VALUES (
            '{PERSON_ID}',
            'Analytics Test Person 2026',
            '{ORGANIZATION_ID}'
        );

        INSERT INTO reference.data_collection (
            id, campaign_id, country_id, questionnaire_id,
            responsible_organization_id, data_collector_id, status
        )
        VALUES (
            '{DATA_COLLECTION_ID}',
            '{CAMPAIGN_ID}',
            '{COUNTRY_ID}',
            '{QUESTIONNAIRE_ID}',
            '{ORGANIZATION_ID}',
            '{PERSON_ID}',
            'SUBMITTED'
        );

        INSERT INTO reference.data_collection_observation (
            id, data_collection_id, questionnaire_variable_id,
            reference_year, numeric_value, observation_status,
            created_at, updated_at
        )
        VALUES
        (
            '{OBSERVATION_B001_ID}',
            '{DATA_COLLECTION_ID}',
            '13c20cf9-58c8-55fc-8d3b-4d6efd639ec2',
            2026, 100, 'PROVIDED',
            CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
        ),
        (
            '{OBSERVATION_B002_ID}',
            '{DATA_COLLECTION_ID}',
            '2e208ad5-474b-5956-b2bb-7c9a9abb8705',
            2026, 200, 'PROVIDED',
            CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
        );

        INSERT INTO reference.data_collection_validation (
            id, data_collection_id, validator_id,
            decision, validated_at, comments
        )
        VALUES
        (
            '{VALIDATION_1_ID}',
            '{DATA_COLLECTION_ID}',
            '{PERSON_ID}',
            'VALIDATED',
            TIMESTAMP '2026-01-15 09:00:00',
            'Analytics test validation - January validated'
        ),
        (
            '{VALIDATION_2_ID}',
            '{DATA_COLLECTION_ID}',
            '{PERSON_ID}',
            'REJECTED',
            TIMESTAMP '2026-01-15 11:00:00',
            'Analytics test validation - January rejected'
        ),
        (
            '{VALIDATION_3_ID}',
            '{DATA_COLLECTION_ID}',
            '{PERSON_ID}',
            'VALIDATED',
            TIMESTAMP '2026-02-15 09:00:00',
            'Analytics test validation - February validated'
        );

        INSERT INTO reference.validation_comment (
            id, observation_id, validator_id,
            comment, severity, created_at, updated_at
        )
        VALUES
        (
            '{COMMENT_1_ID}',
            '{OBSERVATION_B001_ID}',
            '{PERSON_ID}',
            'Analytics test critical comment',
            'CRITICAL',
            TIMESTAMP '2026-01-15 10:00:00',
            TIMESTAMP '2026-01-15 10:00:00'
        ),
        (
            '{COMMENT_2_ID}',
            '{OBSERVATION_B001_ID}',
            '{PERSON_ID}',
            'Analytics test high comment',
            'HIGH',
            TIMESTAMP '2026-01-15 10:30:00',
            TIMESTAMP '2026-01-15 10:30:00'
        ),
        (
            '{COMMENT_3_ID}',
            '{OBSERVATION_B002_ID}',
            '{PERSON_ID}',
            'Analytics test medium comment',
            'MEDIUM',
            TIMESTAMP '2026-02-15 10:00:00',
            TIMESTAMP '2026-02-15 10:00:00'
        );

        COMMIT;

        SELECT
            'data_collection' AS object_type,
            COUNT(*) AS count
        FROM reference.data_collection
        WHERE id = '{DATA_COLLECTION_ID}'

        UNION ALL

        SELECT
            'observations_2026',
            COUNT(*)
        FROM reference.data_collection_observation
        WHERE data_collection_id = '{DATA_COLLECTION_ID}'
          AND reference_year = 2026

        UNION ALL

        SELECT
            'validation_comments',
            COUNT(*)
        FROM reference.validation_comment
        WHERE observation_id IN (
            '{OBSERVATION_B001_ID}', '{OBSERVATION_B002_ID}'
        )

        UNION ALL

        SELECT
            'validation_decisions',
            COUNT(*)
        FROM reference.data_collection_validation
        WHERE data_collection_id = '{DATA_COLLECTION_ID}';

        SELECT
            q.code AS questionnaire,
            qv.series_code AS variable,
            dco.reference_year,
            dco.numeric_value,
            dco.observation_status
        FROM reference.data_collection_observation dco
        JOIN metadata.questionnaire_variable qv
          ON qv.id = dco.questionnaire_variable_id
        JOIN metadata.questionnaire q
          ON q.id = qv.questionnaire_id
        WHERE dco.data_collection_id = '{DATA_COLLECTION_ID}'
        ORDER BY qv.series_code;
    """).strip()

    print("AIKP ANALYTICS 2026 - LOCAL TEST SEED")
    print("Executing against local PostgreSQL...")

    try:
        result = subprocess.run(
            ["psql", "-d", "aikp"],
            input=sql,
            text=True,
            capture_output=True,
            check=False,
        )
    except FileNotFoundError:
        print("ERROR: psql is not available in PATH.", file=sys.stderr)
        return 1

    if result.stdout:
        print(result.stdout)

    if result.returncode != 0:
        if result.stderr:
            print(result.stderr, file=sys.stderr)
        return result.returncode

    print("SEED COMPLETED SUCCESSFULLY.")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
