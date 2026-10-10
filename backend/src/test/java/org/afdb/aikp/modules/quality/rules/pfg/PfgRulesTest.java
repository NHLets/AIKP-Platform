package org.afdb.aikp.modules.quality.rules.pfg;

import static org.assertj.core.api.Assertions.assertThat;
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

class PfgRulesTest {

    private static final int YEAR = 2025;

    private final Pfg001GoodsServicesReconciliationRule pfg001 =
            new Pfg001GoodsServicesReconciliationRule();

    private final Pfg002ForeignInterestRule pfg002 =
            new Pfg002ForeignInterestRule();

    private final Pfg003ReplacementPpeCostRule pfg003 =
            new Pfg003ReplacementPpeCostRule();

    private final Pfg004GovernmentDividendsRule pfg004 =
            new Pfg004GovernmentDividendsRule();

    private final Pfg005TotalAssetsReconciliationRule pfg005 =
            new Pfg005TotalAssetsReconciliationRule();

    private final Pfg006LiabilitiesEquityReconciliationRule pfg006 =
            new Pfg006LiabilitiesEquityReconciliationRule();

    @Test
    void pfg007_accumulatedDepreciationBelowGrossValue_passes() {
        var rule = new Pfg007AccumulatedRehabilitationDepreciationRule();

        var result = rule.evaluate(context(Map.of(
            "F171", observation("F171", "100"),
            "F172", observation("F172", "40")
        )));

        assertThat(result.status()).isEqualTo(QualityFindingStatus.PASSED);
    }

    @Test
    void pfg007_accumulatedDepreciationEqualGrossValue_passes() {
        var rule = new Pfg007AccumulatedRehabilitationDepreciationRule();

        var result = rule.evaluate(context(Map.of(
            "F171", observation("F171", "100"),
            "F172", observation("F172", "100")
        )));

        assertThat(result.status()).isEqualTo(QualityFindingStatus.PASSED);
    }

    @Test
    void pfg007_accumulatedDepreciationAboveGrossValue_fails() {
        var rule = new Pfg007AccumulatedRehabilitationDepreciationRule();

        var result = rule.evaluate(context(Map.of(
            "F171", observation("F171", "100"),
            "F172", observation("F172", "101")
        )));

        assertThat(result.status()).isEqualTo(QualityFindingStatus.FAILED);
        assertThat(result.title()).contains("exceeds");
    }

    @Test
    void pfg007_missingGrossValue_isNotEvaluable() {
        var rule = new Pfg007AccumulatedRehabilitationDepreciationRule();

        var result = rule.evaluate(context(Map.of(
            "F172", observation("F172", "40")
        )));

        assertThat(result.status()).isEqualTo(QualityFindingStatus.NOT_EVALUABLE);
    }

    @Test
    void pfg008_accumulatedDepreciationBelowGrossPpeValue_passes() {
        var rule = new Pfg008AccumulatedPpeDepreciationRule();

        var result = rule.evaluate(context(Map.of(
            "F173", observation("F173", "100"),
            "F174", observation("F174", "40")
        )));

        assertThat(result.status()).isEqualTo(QualityFindingStatus.PASSED);
    }

    @Test
    void pfg008_accumulatedDepreciationEqualGrossPpeValue_passes() {
        var rule = new Pfg008AccumulatedPpeDepreciationRule();

        var result = rule.evaluate(context(Map.of(
            "F173", observation("F173", "100"),
            "F174", observation("F174", "100")
        )));

        assertThat(result.status()).isEqualTo(QualityFindingStatus.PASSED);
    }

    @Test
    void pfg008_accumulatedDepreciationAboveGrossPpeValue_fails() {
        var rule = new Pfg008AccumulatedPpeDepreciationRule();

        var result = rule.evaluate(context(Map.of(
            "F173", observation("F173", "100"),
            "F174", observation("F174", "101")
        )));

        assertThat(result.status()).isEqualTo(QualityFindingStatus.FAILED);
        assertThat(result.title()).contains("exceeds");
    }

    @Test
    void pfg008_missingGrossPpeValue_isNotEvaluable() {
        var rule = new Pfg008AccumulatedPpeDepreciationRule();

        var result = rule.evaluate(context(Map.of(
            "F174", observation("F174", "40")
        )));

        assertThat(result.status()).isEqualTo(QualityFindingStatus.NOT_EVALUABLE);
    }

    @Test
    void pfg009_foreignCurrentLiabilitiesBelowTotal_passes() {
        var rule = new Pfg009ForeignCurrentLiabilitiesRule();

        var result = rule.evaluate(context(Map.of(
            "F176", observation("F176", "100"),
            "F177", observation("F177", "40")
        )));

        assertThat(result.status()).isEqualTo(QualityFindingStatus.PASSED);
    }

    @Test
    void pfg009_foreignCurrentLiabilitiesEqualTotal_passes() {
        var rule = new Pfg009ForeignCurrentLiabilitiesRule();

        var result = rule.evaluate(context(Map.of(
            "F176", observation("F176", "100"),
            "F177", observation("F177", "100")
        )));

        assertThat(result.status()).isEqualTo(QualityFindingStatus.PASSED);
    }

    @Test
    void pfg009_foreignCurrentLiabilitiesAboveTotal_fails() {
        var rule = new Pfg009ForeignCurrentLiabilitiesRule();

        var result = rule.evaluate(context(Map.of(
            "F176", observation("F176", "100"),
            "F177", observation("F177", "101")
        )));

        assertThat(result.status()).isEqualTo(QualityFindingStatus.FAILED);
        assertThat(result.title()).contains("exceeds");
    }

    @Test
    void pfg009_missingTotalCurrentLiabilities_isNotEvaluable() {
        var rule = new Pfg009ForeignCurrentLiabilitiesRule();

        var result = rule.evaluate(context(Map.of(
            "F177", observation("F177", "40")
        )));

        assertThat(result.status()).isEqualTo(QualityFindingStatus.NOT_EVALUABLE);
    }

    @Test
    void pfg010_foreignLongTermLiabilitiesBelowTotal_passes() {
        var rule = new Pfg010ForeignLongTermLiabilitiesRule();

        var result = rule.evaluate(context(Map.of(
            "F178", observation("F178", "100"),
            "F179", observation("F179", "40")
        )));

        assertThat(result.status()).isEqualTo(QualityFindingStatus.PASSED);
    }

    @Test
    void pfg010_foreignLongTermLiabilitiesEqualTotal_passes() {
        var rule = new Pfg010ForeignLongTermLiabilitiesRule();

        var result = rule.evaluate(context(Map.of(
            "F178", observation("F178", "100"),
            "F179", observation("F179", "100")
        )));

        assertThat(result.status()).isEqualTo(QualityFindingStatus.PASSED);
    }

    @Test
    void pfg010_foreignLongTermLiabilitiesAboveTotal_fails() {
        var rule = new Pfg010ForeignLongTermLiabilitiesRule();

        var result = rule.evaluate(context(Map.of(
            "F178", observation("F178", "100"),
            "F179", observation("F179", "101")
        )));

        assertThat(result.status()).isEqualTo(QualityFindingStatus.FAILED);
        assertThat(result.title()).contains("exceeds");
    }

    @Test
    void pfg010_missingTotalLongTermLiabilities_isNotEvaluable() {
        var rule = new Pfg010ForeignLongTermLiabilitiesRule();

        var result = rule.evaluate(context(Map.of(
            "F179", observation("F179", "40")
        )));

        assertThat(result.status()).isEqualTo(QualityFindingStatus.NOT_EVALUABLE);
    }

    @Test
    void pfg011_exactReconciliation_passes() {
        var rule = new Pfg011NetIncomeReconciliationRule();

        var result = rule.evaluate(context(Map.of(
            "F152", observation("F152", "100"),
            "F153", observation("F153", "30"),
            "F155", observation("F155", "70")
        )));

        assertThat(result.status()).isEqualTo(QualityFindingStatus.PASSED);
    }

    @Test
    void pfg011_differenceWithinTolerance_passes() {
        var rule = new Pfg011NetIncomeReconciliationRule();

        var result = rule.evaluate(context(Map.of(
            "F152", observation("F152", "100"),
            "F153", observation("F153", "30"),
            "F155", observation("F155", "70.003")
        )));

        assertThat(result.status()).isEqualTo(QualityFindingStatus.PASSED);
    }

    @Test
    void pfg011_differenceOutsideTolerance_fails() {
        var rule = new Pfg011NetIncomeReconciliationRule();

        var result = rule.evaluate(context(Map.of(
            "F152", observation("F152", "100"),
            "F153", observation("F153", "30"),
            "F155", observation("F155", "75")
        )));

        assertThat(result.status()).isEqualTo(QualityFindingStatus.FAILED);
        assertThat(result.title()).contains("inconsistent");
    }

    @Test
    void pfg011_missingCorporateIncomeTax_isNotEvaluable() {
        var rule = new Pfg011NetIncomeReconciliationRule();

        var result = rule.evaluate(context(Map.of(
            "F152", observation("F152", "100"),
            "F155", observation("F155", "70")
        )));

        assertThat(result.status()).isEqualTo(QualityFindingStatus.NOT_EVALUABLE);
    }

    @Test
    void pfg001_exactReconciliation_passes() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("F132", observation("F132", "100"));
        observations.put("F133", observation("F133", "20"));
        observations.put("F134", observation("F134", "30"));
        observations.put("F135", observation("F135", "50"));

        RuleEvaluation result = pfg001.evaluate(context(observations));

        assertEquals(QualityFindingStatus.PASSED, result.status());
    }

    @Test
    void pfg001_differenceWithinTolerance_passes() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("F132", observation("F132", "100.004"));
        observations.put("F133", observation("F133", "20"));
        observations.put("F134", observation("F134", "30"));
        observations.put("F135", observation("F135", "50"));

        RuleEvaluation result = pfg001.evaluate(context(observations));

        assertEquals(QualityFindingStatus.PASSED, result.status());
    }

    @Test
    void pfg001_differenceOutsideTolerance_fails() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("F132", observation("F132", "101"));
        observations.put("F133", observation("F133", "20"));
        observations.put("F134", observation("F134", "30"));
        observations.put("F135", observation("F135", "50"));

        RuleEvaluation result = pfg001.evaluate(context(observations));

        assertEquals(QualityFindingStatus.FAILED, result.status());
        assertTrue(result.title().contains("inconsistent"));
    }

    @Test
    void pfg001_missingComponent_isNotEvaluable() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("F132", observation("F132", "100"));
        observations.put("F133", observation("F133", "20"));
        observations.put("F134", observation("F134", "30"));

        RuleEvaluation result = pfg001.evaluate(context(observations));

        assertEquals(QualityFindingStatus.NOT_EVALUABLE, result.status());
    }

    @Test
    void pfg002_foreignInterestBelowTotalInterest_passes() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("F141", observation("F141", "100"));
        observations.put("F142", observation("F142", "40"));

        RuleEvaluation result = pfg002.evaluate(context(observations));

        assertEquals(QualityFindingStatus.PASSED, result.status());
    }

    @Test
    void pfg002_foreignInterestEqualTotalInterest_passes() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("F141", observation("F141", "100"));
        observations.put("F142", observation("F142", "100"));

        RuleEvaluation result = pfg002.evaluate(context(observations));

        assertEquals(QualityFindingStatus.PASSED, result.status());
    }

    @Test
    void pfg002_foreignInterestAboveTotalInterest_fails() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("F141", observation("F141", "100"));
        observations.put("F142", observation("F142", "101"));

        RuleEvaluation result = pfg002.evaluate(context(observations));

        assertEquals(QualityFindingStatus.FAILED, result.status());
        assertTrue(result.title().contains("exceeds"));
    }

    @Test
    void pfg002_missingTotalInterest_isNotEvaluable() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("F142", observation("F142", "40"));

        RuleEvaluation result = pfg002.evaluate(context(observations));

        assertEquals(QualityFindingStatus.NOT_EVALUABLE, result.status());
    }

    @Test
    void pfg003_replacementPpeCostBelowTotalPpeCost_passes() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("F160", observation("F160", "100"));
        observations.put("F161", observation("F161", "40"));

        RuleEvaluation result = pfg003.evaluate(context(observations));

        assertEquals(QualityFindingStatus.PASSED, result.status());
    }

    @Test
    void pfg003_replacementPpeCostEqualTotalPpeCost_passes() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("F160", observation("F160", "100"));
        observations.put("F161", observation("F161", "100"));

        RuleEvaluation result = pfg003.evaluate(context(observations));

        assertEquals(QualityFindingStatus.PASSED, result.status());
    }

    @Test
    void pfg003_replacementPpeCostAboveTotalPpeCost_fails() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("F160", observation("F160", "100"));
        observations.put("F161", observation("F161", "101"));

        RuleEvaluation result = pfg003.evaluate(context(observations));

        assertEquals(QualityFindingStatus.FAILED, result.status());
        assertTrue(result.title().contains("exceeds"));
    }

    @Test
    void pfg003_missingTotalPpeCost_isNotEvaluable() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("F161", observation("F161", "40"));

        RuleEvaluation result = pfg003.evaluate(context(observations));

        assertEquals(QualityFindingStatus.NOT_EVALUABLE, result.status());
    }

    @Test
    void pfg004_governmentDividendsBelowTotalDividends_passes() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("F165", observation("F165", "100"));
        observations.put("F166", observation("F166", "40"));

        RuleEvaluation result = pfg004.evaluate(context(observations));

        assertEquals(QualityFindingStatus.PASSED, result.status());
    }

    @Test
    void pfg004_governmentDividendsEqualTotalDividends_passes() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("F165", observation("F165", "100"));
        observations.put("F166", observation("F166", "100"));

        RuleEvaluation result = pfg004.evaluate(context(observations));

        assertEquals(QualityFindingStatus.PASSED, result.status());
    }

    @Test
    void pfg004_governmentDividendsAboveTotalDividends_fails() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("F165", observation("F165", "100"));
        observations.put("F166", observation("F166", "101"));

        RuleEvaluation result = pfg004.evaluate(context(observations));

        assertEquals(QualityFindingStatus.FAILED, result.status());
        assertTrue(result.title().contains("exceeds"));
    }

    @Test
    void pfg004_missingTotalDividends_isNotEvaluable() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("F166", observation("F166", "40"));

        RuleEvaluation result = pfg004.evaluate(context(observations));

        assertEquals(QualityFindingStatus.NOT_EVALUABLE, result.status());
    }

    @Test
    void pfg005_exactReconciliation_passes() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("F169", observation("F169", "400"));
        observations.put("F170", observation("F170", "600"));
        observations.put("F175", observation("F175", "1000"));

        RuleEvaluation result = pfg005.evaluate(context(observations));

        assertEquals(QualityFindingStatus.PASSED, result.status());
    }

    @Test
    void pfg005_differenceWithinTolerance_passes() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("F169", observation("F169", "400"));
        observations.put("F170", observation("F170", "600"));
        observations.put("F175", observation("F175", "1000.004"));

        RuleEvaluation result = pfg005.evaluate(context(observations));

        assertEquals(QualityFindingStatus.PASSED, result.status());
    }

    @Test
    void pfg005_differenceOutsideTolerance_fails() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("F169", observation("F169", "400"));
        observations.put("F170", observation("F170", "600"));
        observations.put("F175", observation("F175", "1010"));

        RuleEvaluation result = pfg005.evaluate(context(observations));

        assertEquals(QualityFindingStatus.FAILED, result.status());
        assertTrue(result.title().contains("inconsistent"));
    }

    @Test
    void pfg005_missingComponent_isNotEvaluable() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("F169", observation("F169", "400"));
        observations.put("F175", observation("F175", "1000"));

        RuleEvaluation result = pfg005.evaluate(context(observations));

        assertEquals(QualityFindingStatus.NOT_EVALUABLE, result.status());
    }

    @Test
    void pfg006_exactReconciliation_passes() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("F176", observation("F176", "200"));
        observations.put("F178", observation("F178", "300"));
        observations.put("F180", observation("F180", "500"));
        observations.put("F182", observation("F182", "1000"));

        RuleEvaluation result = pfg006.evaluate(context(observations));

        assertEquals(QualityFindingStatus.PASSED, result.status());
    }

    @Test
    void pfg006_differenceWithinTolerance_passes() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("F176", observation("F176", "200"));
        observations.put("F178", observation("F178", "300"));
        observations.put("F180", observation("F180", "500"));
        observations.put("F182", observation("F182", "1000.004"));

        RuleEvaluation result = pfg006.evaluate(context(observations));

        assertEquals(QualityFindingStatus.PASSED, result.status());
    }

    @Test
    void pfg006_differenceOutsideTolerance_fails() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("F176", observation("F176", "200"));
        observations.put("F178", observation("F178", "300"));
        observations.put("F180", observation("F180", "500"));
        observations.put("F182", observation("F182", "1010"));

        RuleEvaluation result = pfg006.evaluate(context(observations));

        assertEquals(QualityFindingStatus.FAILED, result.status());
        assertTrue(result.title().contains("inconsistent"));
    }

    @Test
    void pfg006_missingComponent_isNotEvaluable() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("F176", observation("F176", "200"));
        observations.put("F178", observation("F178", "300"));
        observations.put("F180", observation("F180", "500"));

        RuleEvaluation result = pfg006.evaluate(context(observations));

        assertEquals(QualityFindingStatus.NOT_EVALUABLE, result.status());
    }

    private QualityEvaluationContext context(
            Map<String, DataCollectionObservation> observations) {

        Map<String, QuestionnaireVariable> variables = new HashMap<>();
        Map<String, List<DataCollectionObservation>> grouped = new HashMap<>();

        for (Map.Entry<String, DataCollectionObservation> entry : observations.entrySet()) {
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
                "LCU million",
                false,
                1,
                true);
    }

    private DataCollectionObservation observation(String code, String value) {
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
                "LCU million",
                null);
    }
}
