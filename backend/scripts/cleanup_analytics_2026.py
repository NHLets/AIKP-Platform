#!/usr/bin/env python3

from __future__ import annotations

import subprocess
import sys
from textwrap import dedent


DATA_COLLECTION_ID = "11111111-2026-4000-8000-000000000004"

OBSERVATION_B001_ID = "11111111-2026-4000-8000-000000000005"
OBSERVATION_B002_ID = "11111111-2026-4000-8000-000000000006"

COMMENT_1_ID = "11111111-2026-4000-8000-000000000007"
COMMENT_2_ID = "11111111-2026-4000-8000-000000000008"
COMMENT_3_ID = "11111111-2026-4000-8000-000000000009"

VALIDATION_1_ID = "11111111-2026-4000-8000-000000000010"
VALIDATION_2_ID = "11111111-2026-4000-8000-000000000011"
VALIDATION_3_ID = "11111111-2026-4000-8000-000000000012"

PERSON_ID = "11111111-2026-4000-8000-000000000003"
ORGANIZATION_ID = "11111111-2026-4000-8000-000000000002"
COUNTRY_ID = "11111111-2026-4000-8000-000000000001"


def main() -> int:
    sql = dedent(f"""
        BEGIN;

        DO $$
        DECLARE
            db_name text;
            server_addr inet;
        BEGIN
            SELECT current_database(), inet_server_addr()
              INTO db_name, server_addr;

            IF db_name <> 'aikp' THEN
                RAISE EXCEPTION
                    'SAFETY STOP: expected database aikp, got %',
                    db_name;
            END IF;

            IF server_addr IS NOT NULL
               AND server_addr NOT IN ('127.0.0.1'::inet, '::1'::inet) THEN
                RAISE EXCEPTION
                    'SAFETY STOP: database server is not local: %',
                    server_addr;
            END IF;
        END $$;

        -- Safety verification: all deterministic test records must still
        -- correspond to the Analytics 2026 test dataset.

        DO $$
        BEGIN
            IF EXISTS (
                SELECT 1
                FROM reference.data_collection
                WHERE id = '{DATA_COLLECTION_ID}'
                  AND (
                      campaign_id <> 'e834a2a1-52a8-4d89-8795-f94b4f699a04'::uuid
                      OR country_id <> '{COUNTRY_ID}'::uuid
                      OR questionnaire_id <> '45b2179c-6214-5b12-972f-1068923578c1'::uuid
                  )
            ) THEN
                RAISE EXCEPTION
                    'SAFETY STOP: deterministic data collection does not match Analytics 2026 seed';
            END IF;
        END $$;

        DELETE FROM reference.validation_comment
        WHERE id IN (
            '{COMMENT_1_ID}',
            '{COMMENT_2_ID}',
            '{COMMENT_3_ID}'
        );

        DELETE FROM reference.data_collection_validation
        WHERE id IN (
            '{VALIDATION_1_ID}',
            '{VALIDATION_2_ID}',
            '{VALIDATION_3_ID}'
        );

        DELETE FROM reference.data_collection_observation
        WHERE id IN (
            '{OBSERVATION_B001_ID}',
            '{OBSERVATION_B002_ID}'
        );

        DELETE FROM reference.data_collection
        WHERE id = '{DATA_COLLECTION_ID}';

        DELETE FROM reference.person
        WHERE id = '{PERSON_ID}';

        DELETE FROM reference.organization
        WHERE id = '{ORGANIZATION_ID}';

        DELETE FROM reference.country
        WHERE id = '{COUNTRY_ID}';

        COMMIT;

        SELECT
            'data_collection' AS object_type,
            COUNT(*) AS remaining
        FROM reference.data_collection
        WHERE id = '{DATA_COLLECTION_ID}'

        UNION ALL

        SELECT
            'observations',
            COUNT(*)
        FROM reference.data_collection_observation
        WHERE id IN (
            '{OBSERVATION_B001_ID}',
            '{OBSERVATION_B002_ID}'
        )

        UNION ALL

        SELECT
            'validation_comments',
            COUNT(*)
        FROM reference.validation_comment
        WHERE id IN (
            '{COMMENT_1_ID}',
            '{COMMENT_2_ID}',
            '{COMMENT_3_ID}'
        )

        UNION ALL

        SELECT
            'validation_decisions',
            COUNT(*)
        FROM reference.data_collection_validation
        WHERE id IN (
            '{VALIDATION_1_ID}',
            '{VALIDATION_2_ID}',
            '{VALIDATION_3_ID}'
        )

        UNION ALL

        SELECT
            'person',
            COUNT(*)
        FROM reference.person
        WHERE id = '{PERSON_ID}'

        UNION ALL

        SELECT
            'organization',
            COUNT(*)
        FROM reference.organization
        WHERE id = '{ORGANIZATION_ID}'

        UNION ALL

        SELECT
            'country',
            COUNT(*)
        FROM reference.country
        WHERE id = '{COUNTRY_ID}';
    """).strip()

    print("AIKP ANALYTICS 2026 - LOCAL TEST CLEANUP")
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

    print("CLEANUP COMPLETED SUCCESSFULLY.")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
