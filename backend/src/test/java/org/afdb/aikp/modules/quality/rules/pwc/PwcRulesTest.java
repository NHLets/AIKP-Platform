package org.afdb.aikp.modules.quality.rules.pwc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.afdb.aikp.modules.collection.domain.model.DataCollectionObservation;
import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionId;
import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionObservationId;
import org.afdb.aikp.modules.questionnaire.domain.enums.QuestionnaireVariableDataType;
import org.afdb.aikp.modules.questionnaire.domain.model.QuestionnaireVariable;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireId;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireVariableId;
import org.afdb.aikp.modules.quality.domain.enums.QualityFindingStatus;
import org.afdb.aikp.modules.quality.domain.model.RuleEvaluation;
import org.afdb.aikp.modules.quality.domain.service.QualityEvaluationContext;
import org.junit.jupiter.api.Test;

class PwcRulesTest {

    private static final int YEAR = 2025;

    private final Pwc002OperationalMetersRule pwc002 =
            new Pwc002OperationalMetersRule();

    private final Pwc003PrepaymentMetersRule pwc003 =
            new Pwc003PrepaymentMetersRule();

    private final Pwc004SystemLossReconciliationRule pwc004 =
            new Pwc004SystemLossReconciliationRule();

    private final Pwc005TechnicalLossReconciliationRule pwc005 =
            new Pwc005TechnicalLossReconciliationRule();

    private final Pwc006CollectionRatioRule pwc006 =
            new Pwc006CollectionRatioRule();

    private final Pwc007OperationalCostComponentsRule pwc007 =
            new Pwc007OperationalCostComponentsRule();

    private final Pwc008RehabilitationCostRule pwc008 =
            new Pwc008RehabilitationCostRule();

    private final Pwc009NewAssetCostRule pwc009 =
            new Pwc009NewAssetCostRule();

    private final Pwc001CustomerReconciliationRule pwc001 =
            new Pwc001CustomerReconciliationRule();

    @Test
    void pwc009_newAssetCostBelowTotalCapitalCost_passes() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B081", observation("B081", "100"));
        observations.put("B083", observation("B083", "80"));

        RuleEvaluation result = pwc009.evaluate(context(observations));

        assertEquals(QualityFindingStatus.PASSED, result.status());
    }

    @Test
    void pwc009_newAssetCostEqualTotalCapitalCost_passes() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B081", observation("B081", "100"));
        observations.put("B083", observation("B083", "100"));

        RuleEvaluation result = pwc009.evaluate(context(observations));

        assertEquals(QualityFindingStatus.PASSED, result.status());
    }

    @Test
    void pwc009_newAssetCostAboveTotalCapitalCost_fails() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B081", observation("B081", "100"));
        observations.put("B083", observation("B083", "101"));

        RuleEvaluation result = pwc009.evaluate(context(observations));

        assertEquals(QualityFindingStatus.FAILED, result.status());
        assertTrue(result.title().contains("exceeds"));
    }

    @Test
    void pwc009_missingTotalCapitalCost_isNotEvaluable() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B083", observation("B083", "80"));

        RuleEvaluation result = pwc009.evaluate(context(observations));

        assertEquals(QualityFindingStatus.NOT_EVALUABLE, result.status());
    }

    @Test
    void pwc008_rehabilitationCostBelowTotalCapitalCost_passes() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B081", observation("B081", "100"));
        observations.put("B082", observation("B082", "80"));

        RuleEvaluation result = pwc008.evaluate(context(observations));

        assertEquals(QualityFindingStatus.PASSED, result.status());
    }

    @Test
    void pwc008_rehabilitationCostEqualTotalCapitalCost_passes() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B081", observation("B081", "100"));
        observations.put("B082", observation("B082", "100"));

        RuleEvaluation result = pwc008.evaluate(context(observations));

        assertEquals(QualityFindingStatus.PASSED, result.status());
    }

    @Test
    void pwc008_rehabilitationCostAboveTotalCapitalCost_fails() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B081", observation("B081", "100"));
        observations.put("B082", observation("B082", "101"));

        RuleEvaluation result = pwc008.evaluate(context(observations));

        assertEquals(QualityFindingStatus.FAILED, result.status());
        assertTrue(result.title().contains("exceeds"));
    }

    @Test
    void pwc008_missingTotalCapitalCost_isNotEvaluable() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B082", observation("B082", "80"));

        RuleEvaluation result = pwc008.evaluate(context(observations));

        assertEquals(QualityFindingStatus.NOT_EVALUABLE, result.status());
    }

    @Test
    void pwc007_componentsBelowOperationalCost_passes() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B076", observation("B076", "100"));
        observations.put("B077", observation("B077", "30"));
        observations.put("B078", observation("B078", "20"));
        observations.put("B079", observation("B079", "40"));

        RuleEvaluation result = pwc007.evaluate(context(observations));

        assertEquals(QualityFindingStatus.PASSED, result.status());
    }

    @Test
    void pwc007_componentsEqualOperationalCost_passes() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B076", observation("B076", "100"));
        observations.put("B077", observation("B077", "30"));
        observations.put("B078", observation("B078", "20"));
        observations.put("B079", observation("B079", "50"));

        RuleEvaluation result = pwc007.evaluate(context(observations));

        assertEquals(QualityFindingStatus.PASSED, result.status());
    }

    @Test
    void pwc007_componentsAboveOperationalCost_fails() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B076", observation("B076", "100"));
        observations.put("B077", observation("B077", "40"));
        observations.put("B078", observation("B078", "30"));
        observations.put("B079", observation("B079", "40"));

        RuleEvaluation result = pwc007.evaluate(context(observations));

        assertEquals(QualityFindingStatus.FAILED, result.status());
        assertTrue(result.title().contains("exceeds"));
    }

    @Test
    void pwc007_missingComponent_isNotEvaluable() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B076", observation("B076", "100"));
        observations.put("B077", observation("B077", "30"));
        observations.put("B078", observation("B078", "20"));

        RuleEvaluation result = pwc007.evaluate(context(observations));

        assertEquals(QualityFindingStatus.NOT_EVALUABLE, result.status());
    }

    @Test
    void pwc006_collectedAmountBelowBilledAmount_passes() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B061", observation("B061", "80"));
        observations.put("B069", observation("B069", "100"));

        RuleEvaluation result = pwc006.evaluate(context(observations));

        assertEquals(QualityFindingStatus.PASSED, result.status());
    }

    @Test
    void pwc006_collectedAmountEqualBilledAmount_passes() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B061", observation("B061", "100"));
        observations.put("B069", observation("B069", "100"));

        RuleEvaluation result = pwc006.evaluate(context(observations));

        assertEquals(QualityFindingStatus.PASSED, result.status());
    }

    @Test
    void pwc006_collectedAmountAboveBilledAmount_fails() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B061", observation("B061", "101"));
        observations.put("B069", observation("B069", "100"));

        RuleEvaluation result = pwc006.evaluate(context(observations));

        assertEquals(QualityFindingStatus.FAILED, result.status());
        assertTrue(result.title().contains("exceeds"));
    }

    @Test
    void pwc006_missingBilledAmount_isNotEvaluable() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B061", observation("B061", "80"));

        RuleEvaluation result = pwc006.evaluate(context(observations));

        assertEquals(QualityFindingStatus.NOT_EVALUABLE, result.status());
    }

    @Test
    void pwc005_exactReconciliation_passes() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B053", observation("B053", "100"));
        observations.put("B055", observation("B055", "40"));
        observations.put("B056", observation("B056", "60"));

        RuleEvaluation result = pwc005.evaluate(context(observations));

        assertEquals(QualityFindingStatus.PASSED, result.status());
    }

    @Test
    void pwc005_differenceWithinTolerance_passes() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B053", observation("B053", "100.004"));
        observations.put("B055", observation("B055", "40"));
        observations.put("B056", observation("B056", "60"));

        RuleEvaluation result = pwc005.evaluate(context(observations));

        assertEquals(QualityFindingStatus.PASSED, result.status());
    }

    @Test
    void pwc005_differenceOutsideTolerance_fails() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B053", observation("B053", "101"));
        observations.put("B055", observation("B055", "40"));
        observations.put("B056", observation("B056", "60"));

        RuleEvaluation result = pwc005.evaluate(context(observations));

        assertEquals(QualityFindingStatus.FAILED, result.status());
        assertTrue(result.title().contains("inconsistent"));
    }

    @Test
    void pwc005_missingComponent_isNotEvaluable() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B053", observation("B053", "100"));
        observations.put("B055", observation("B055", "40"));

        RuleEvaluation result = pwc005.evaluate(context(observations));

        assertEquals(QualityFindingStatus.NOT_EVALUABLE, result.status());
    }

    @Test
    void pwc004_exactReconciliation_passes() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B052", observation("B052", "100"));
        observations.put("B053", observation("B053", "70"));
        observations.put("B054", observation("B054", "30"));

        RuleEvaluation result = pwc004.evaluate(context(observations));

        assertEquals(QualityFindingStatus.PASSED, result.status());
    }

    @Test
    void pwc004_differenceWithinTolerance_passes() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B052", observation("B052", "100.004"));
        observations.put("B053", observation("B053", "70"));
        observations.put("B054", observation("B054", "30"));

        RuleEvaluation result = pwc004.evaluate(context(observations));

        assertEquals(QualityFindingStatus.PASSED, result.status());
    }

    @Test
    void pwc004_differenceOutsideTolerance_fails() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B052", observation("B052", "101"));
        observations.put("B053", observation("B053", "70"));
        observations.put("B054", observation("B054", "30"));

        RuleEvaluation result = pwc004.evaluate(context(observations));

        assertEquals(QualityFindingStatus.FAILED, result.status());
        assertTrue(result.title().contains("inconsistent"));
    }

    @Test
    void pwc004_missingComponent_isNotEvaluable() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B052", observation("B052", "100"));
        observations.put("B053", observation("B053", "70"));

        RuleEvaluation result = pwc004.evaluate(context(observations));

        assertEquals(QualityFindingStatus.NOT_EVALUABLE, result.status());
    }

    @Test
    void pwc003_operationalPrepaymentMetersBelowPrepaymentMeters_passes() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B050", observation("B050", "100"));
        observations.put("B051", observation("B051", "80"));

        RuleEvaluation result = pwc003.evaluate(context(observations));

        assertEquals(QualityFindingStatus.PASSED, result.status());
    }

    @Test
    void pwc003_operationalPrepaymentMetersEqualPrepaymentMeters_passes() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B050", observation("B050", "100"));
        observations.put("B051", observation("B051", "100"));

        RuleEvaluation result = pwc003.evaluate(context(observations));

        assertEquals(QualityFindingStatus.PASSED, result.status());
    }

    @Test
    void pwc003_operationalPrepaymentMetersAbovePrepaymentMeters_fails() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B050", observation("B050", "100"));
        observations.put("B051", observation("B051", "101"));

        RuleEvaluation result = pwc003.evaluate(context(observations));

        assertEquals(QualityFindingStatus.FAILED, result.status());
        assertTrue(result.title().contains("exceeds"));
    }

    @Test
    void pwc003_missingPrepaymentMeters_isNotEvaluable() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B051", observation("B051", "80"));

        RuleEvaluation result = pwc003.evaluate(context(observations));

        assertEquals(QualityFindingStatus.NOT_EVALUABLE, result.status());
    }

    @Test
    void pwc002_operationalMetersBelowInstalledMeters_passes() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B048", observation("B048", "100"));
        observations.put("B049", observation("B049", "80"));

        RuleEvaluation result = pwc002.evaluate(context(observations));

        assertEquals(QualityFindingStatus.PASSED, result.status());
    }

    @Test
    void pwc002_operationalMetersEqualInstalledMeters_passes() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B048", observation("B048", "100"));
        observations.put("B049", observation("B049", "100"));

        RuleEvaluation result = pwc002.evaluate(context(observations));

        assertEquals(QualityFindingStatus.PASSED, result.status());
    }

    @Test
    void pwc002_operationalMetersAboveInstalledMeters_fails() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B048", observation("B048", "100"));
        observations.put("B049", observation("B049", "101"));

        RuleEvaluation result = pwc002.evaluate(context(observations));

        assertEquals(QualityFindingStatus.FAILED, result.status());
        assertTrue(result.title().contains("exceeds"));
    }

    @Test
    void pwc002_missingInstalledMeters_isNotEvaluable() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B049", observation("B049", "80"));

        RuleEvaluation result = pwc002.evaluate(context(observations));

        assertEquals(QualityFindingStatus.NOT_EVALUABLE, result.status());
    }

    @Test
    void pwc001_exactReconciliation_passes() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("A214", observation("A214", "100"));
        observations.put("A261", observation("A261", "60"));
        observations.put("A262", observation("A262", "40"));

        RuleEvaluation result = pwc001.evaluate(context(observations));

        assertEquals(QualityFindingStatus.PASSED, result.status());
    }

    @Test
    void pwc001_differenceWithinTolerance_passes() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("A214", observation("A214", "100.004"));
        observations.put("A261", observation("A261", "60"));
        observations.put("A262", observation("A262", "40"));

        RuleEvaluation result = pwc001.evaluate(context(observations));

        assertEquals(QualityFindingStatus.PASSED, result.status());
    }

    @Test
    void pwc001_differenceOutsideTolerance_fails() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("A214", observation("A214", "101"));
        observations.put("A261", observation("A261", "60"));
        observations.put("A262", observation("A262", "40"));

        RuleEvaluation result = pwc001.evaluate(context(observations));

        assertEquals(QualityFindingStatus.FAILED, result.status());
        assertTrue(result.title().contains("inconsistent"));
    }

    @Test
    void pwc001_missingComponent_isNotEvaluable() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("A214", observation("A214", "100"));
        observations.put("A261", observation("A261", "60"));

        RuleEvaluation result = pwc001.evaluate(context(observations));

        assertEquals(QualityFindingStatus.NOT_EVALUABLE, result.status());
    }

    private QualityEvaluationContext context(
            Map<String, DataCollectionObservation> observations) {

        Map<String, QuestionnaireVariable> variables = new HashMap<>();
        Map<String, List<DataCollectionObservation>> grouped =
                new HashMap<>();

        for (Map.Entry<String, DataCollectionObservation> entry
                : observations.entrySet()) {

            String code = entry.getKey();

            variables.put(code, questionnaireVariable(code));
            grouped.put(code, List.of(entry.getValue()));
        }

        return new QualityEvaluationContext(variables, grouped);
    }

    private QuestionnaireVariable questionnaireVariable(String code) {
        return QuestionnaireVariable.restore(
                QuestionnaireVariableId.generate(),
                QuestionnaireId.generate(),
                null,
                code,
                code,
                null,
                QuestionnaireVariableDataType.NUMBER,
                "MW",
                false,
                1,
                true);
    }

    private DataCollectionObservation observation(
            String code,
            String value) {

        return DataCollectionObservation.provided(
                DataCollectionObservationId.generate(),
                DataCollectionId.generate(),
                QuestionnaireVariableId.generate(),
                YEAR,
                QuestionnaireVariableDataType.NUMBER,
                new BigDecimal(value),
                null,
                null,
                null,
                "MW",
                null);
    }
}
