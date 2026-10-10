-- ============================================================================
-- AIKP Platform
-- Flyway Migration
-- Version : V2.996
-- Aggregate : Metadata / Questionnaire / PW_C
-- Purpose : Correct spreadsheet data-entry units for PW_C
-- ============================================================================

-- FINANCIAL: all variables must use LCU Million.
UPDATE metadata.questionnaire_variable qv
SET unit = 'LCU Million'
FROM metadata.questionnaire q
JOIN metadata.questionnaire_group qg
  ON qg.questionnaire_id = q.id
WHERE qv.questionnaire_id = q.id
  AND qv.questionnaire_group_id = qg.id
  AND q.code = 'PW_C'
  AND qg.code = 'FINANCIAL';

-- TECHNICAL: B052-B056 and B234-B235 must use GWh.
UPDATE metadata.questionnaire_variable qv
SET unit = 'GWh'
FROM metadata.questionnaire q
JOIN metadata.questionnaire_group qg
  ON qg.questionnaire_id = q.id
WHERE qv.questionnaire_id = q.id
  AND qv.questionnaire_group_id = qg.id
  AND q.code = 'PW_C'
  AND qg.code = 'TECHNICAL'
  AND LOWER(qv.series_code) IN (
      'b052',
      'b053',
      'b054',
      'b055',
      'b056',
      'b234',
      'b235'
  );

-- TECHNICAL: B057 must use # people.
UPDATE metadata.questionnaire_variable qv
SET unit = '# people'
FROM metadata.questionnaire q
JOIN metadata.questionnaire_group qg
  ON qg.questionnaire_id = q.id
WHERE qv.questionnaire_id = q.id
  AND qv.questionnaire_group_id = qg.id
  AND q.code = 'PW_C'
  AND qg.code = 'TECHNICAL'
  AND LOWER(qv.series_code) = 'b057';

-- ============================================================================
-- Validation
-- ============================================================================

DO $$
DECLARE
    financial_total INTEGER;
    financial_correct INTEGER;
    technical_gwh_correct INTEGER;
    b057_correct INTEGER;
BEGIN
    SELECT COUNT(*)
      INTO financial_total
      FROM metadata.questionnaire_variable qv
      JOIN metadata.questionnaire q
        ON q.id = qv.questionnaire_id
      JOIN metadata.questionnaire_group qg
        ON qg.id = qv.questionnaire_group_id
     WHERE q.code = 'PW_C'
       AND qg.code = 'FINANCIAL';

    IF financial_total = 0 THEN
        RAISE EXCEPTION
            'V2.996 validation failed: no FINANCIAL variables found for PW_C';
    END IF;

    SELECT COUNT(*)
      INTO financial_correct
      FROM metadata.questionnaire_variable qv
      JOIN metadata.questionnaire q
        ON q.id = qv.questionnaire_id
      JOIN metadata.questionnaire_group qg
        ON qg.id = qv.questionnaire_group_id
     WHERE q.code = 'PW_C'
       AND qg.code = 'FINANCIAL'
       AND qv.unit = 'LCU Million';

    IF financial_correct <> financial_total THEN
        RAISE EXCEPTION
            'V2.996 validation failed: %/% FINANCIAL variables have LCU Million',
            financial_correct,
            financial_total;
    END IF;

    SELECT COUNT(*)
      INTO technical_gwh_correct
      FROM metadata.questionnaire_variable qv
      JOIN metadata.questionnaire q
        ON q.id = qv.questionnaire_id
      JOIN metadata.questionnaire_group qg
        ON qg.id = qv.questionnaire_group_id
     WHERE q.code = 'PW_C'
       AND qg.code = 'TECHNICAL'
       AND LOWER(qv.series_code) IN (
           'b052','b053','b054','b055','b056','b234','b235'
       )
       AND qv.unit = 'GWh';

    IF technical_gwh_correct <> 7 THEN
        RAISE EXCEPTION
            'V2.996 validation failed: expected 7 TECHNICAL variables with GWh, found %',
            technical_gwh_correct;
    END IF;

    SELECT COUNT(*)
      INTO b057_correct
      FROM metadata.questionnaire_variable qv
      JOIN metadata.questionnaire q
        ON q.id = qv.questionnaire_id
      JOIN metadata.questionnaire_group qg
        ON qg.id = qv.questionnaire_group_id
     WHERE q.code = 'PW_C'
       AND qg.code = 'TECHNICAL'
       AND LOWER(qv.series_code) = 'b057'
       AND qv.unit = '# people';

    IF b057_correct <> 1 THEN
        RAISE EXCEPTION
            'V2.996 validation failed: B057 does not have # people';
    END IF;
END $$;
