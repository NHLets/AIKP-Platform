-- AIKP Platform
-- Correct F_G questionnaire units
-- LCU per year -> LCU million

UPDATE metadata.questionnaire_variable qv
SET unit = 'LCU million'
FROM metadata.questionnaire q
WHERE q.id = qv.questionnaire_id
  AND q.code = 'F_G'
  AND qv.unit = 'LCU per year';

DO $$
DECLARE
    lcu_per_year_count INTEGER;
    lcu_million_count INTEGER;
BEGIN
    SELECT COUNT(*)
    INTO lcu_per_year_count
    FROM metadata.questionnaire_variable qv
    JOIN metadata.questionnaire q
      ON q.id = qv.questionnaire_id
    WHERE q.code = 'F_G'
      AND qv.unit = 'LCU per year';

    SELECT COUNT(*)
    INTO lcu_million_count
    FROM metadata.questionnaire_variable qv
    JOIN metadata.questionnaire q
      ON q.id = qv.questionnaire_id
    WHERE q.code = 'F_G'
      AND qv.unit = 'LCU million';

    IF lcu_per_year_count <> 0 THEN
        RAISE EXCEPTION
            'F_G unit correction failed: % variable(s) still use LCU per year',
            lcu_per_year_count;
    END IF;

    IF lcu_million_count <> 53 THEN
        RAISE EXCEPTION
            'F_G unit correction failed: expected 53 variables with LCU million, found %',
            lcu_million_count;
    END IF;

    RAISE NOTICE
        'F_G unit correction validated: % variables use LCU million',
        lcu_million_count;
END $$;
