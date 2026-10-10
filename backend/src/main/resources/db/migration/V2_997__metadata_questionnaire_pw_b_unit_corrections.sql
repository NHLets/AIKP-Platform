-- ============================================================================
-- AIKP Platform
-- Flyway Migration
-- Version : V2.997
-- Aggregate : Metadata / Questionnaire / PW_B
-- Purpose : Correct spreadsheet data-entry units for PW_B
-- ============================================================================

-- TECHNICAL: B001-B012 must use MW.
UPDATE metadata.questionnaire_variable qv
SET unit = 'MW'
FROM metadata.questionnaire q
WHERE qv.questionnaire_id = q.id
  AND q.code = 'PW_B'
  AND LOWER(qv.series_code) IN (
      'b001','b002','b003','b004','b005','b006',
      'b007','b008','b009','b010','b011','b012'
  );

-- TECHNICAL: B013-B023 must use GWh.
UPDATE metadata.questionnaire_variable qv
SET unit = 'GWh'
FROM metadata.questionnaire q
WHERE qv.questionnaire_id = q.id
  AND q.code = 'PW_B'
  AND LOWER(qv.series_code) IN (
      'b013','b014','b015','b016','b017','b018',
      'b019','b020','b021','b022','b023'
  );

-- TECHNICAL: B024-B029 must use km.
UPDATE metadata.questionnaire_variable qv
SET unit = 'km'
FROM metadata.questionnaire q
WHERE qv.questionnaire_id = q.id
  AND q.code = 'PW_B'
  AND LOWER(qv.series_code) IN (
      'b024','b025','b026','b027','b028','b029'
  );

-- TECHNICAL: B034 must use GWh.
UPDATE metadata.questionnaire_variable qv
SET unit = 'GWh'
FROM metadata.questionnaire q
WHERE qv.questionnaire_id = q.id
  AND q.code = 'PW_B'
  AND LOWER(qv.series_code) = 'b034';

-- ============================================================================
-- Validation
-- ============================================================================

DO $$
DECLARE
    mw_correct INTEGER;
    gwh_correct INTEGER;
    km_correct INTEGER;
    b034_correct INTEGER;
BEGIN
    SELECT COUNT(*)
      INTO mw_correct
      FROM metadata.questionnaire_variable qv
      JOIN metadata.questionnaire q
        ON q.id = qv.questionnaire_id
     WHERE q.code = 'PW_B'
       AND LOWER(qv.series_code) IN (
           'b001','b002','b003','b004','b005','b006',
           'b007','b008','b009','b010','b011','b012'
       )
       AND qv.unit = 'MW';

    IF mw_correct <> 11 THEN
        RAISE EXCEPTION
            'V2.997 validation failed: expected 11 existing variables with MW, found %',
            mw_correct;
    END IF;

    SELECT COUNT(*)
      INTO gwh_correct
      FROM metadata.questionnaire_variable qv
      JOIN metadata.questionnaire q
        ON q.id = qv.questionnaire_id
     WHERE q.code = 'PW_B'
       AND LOWER(qv.series_code) IN (
           'b013','b014','b015','b016','b017','b018',
           'b019','b020','b021','b022','b023'
       )
       AND qv.unit = 'GWh';

    IF gwh_correct <> 11 THEN
        RAISE EXCEPTION
            'V2.997 validation failed: expected 11 variables with GWh in B013-B023, found %',
            gwh_correct;
    END IF;

    SELECT COUNT(*)
      INTO km_correct
      FROM metadata.questionnaire_variable qv
      JOIN metadata.questionnaire q
        ON q.id = qv.questionnaire_id
     WHERE q.code = 'PW_B'
       AND LOWER(qv.series_code) IN (
           'b024','b025','b026','b027','b028','b029'
       )
       AND qv.unit = 'km';

    IF km_correct <> 6 THEN
        RAISE EXCEPTION
            'V2.997 validation failed: expected 6 variables with km, found %',
            km_correct;
    END IF;

    SELECT COUNT(*)
      INTO b034_correct
      FROM metadata.questionnaire_variable qv
      JOIN metadata.questionnaire q
        ON q.id = qv.questionnaire_id
     WHERE q.code = 'PW_B'
       AND LOWER(qv.series_code) = 'b034'
       AND qv.unit = 'GWh';

    IF b034_correct <> 1 THEN
        RAISE EXCEPTION
            'V2.997 validation failed: B034 does not have GWh';
    END IF;
END $$;
