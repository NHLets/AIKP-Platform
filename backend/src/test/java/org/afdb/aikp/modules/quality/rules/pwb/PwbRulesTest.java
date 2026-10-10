package org.afdb.aikp.modules.quality.rules.pwb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.afdb.aikp.modules.collection.domain.enums.ObservationStatus;
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

class PwbRulesTest {

    private static final int YEAR = 2025;

    private final Pwb001NonNegativeGenerationCapacityRule pwb001 =
            new Pwb001NonNegativeGenerationCapacityRule();

    private final Pwb002InstalledCapacityReconciliationRule pwb002 =
            new Pwb002InstalledCapacityReconciliationRule();

    private final Pwb003OperationalCapacityRule pwb003 =
            new Pwb003OperationalCapacityRule();

    private final Pwb004OffGridOperationalCapacityRule pwb004 =
            new Pwb004OffGridOperationalCapacityRule();

    private final Pwb005TransmissionRehabilitationLengthRule pwb005 =
            new Pwb005TransmissionRehabilitationLengthRule();

    private final Pwb006GenerationReconciliationRule pwb006 =
            new Pwb006GenerationReconciliationRule();

    private final Pwb007MvRehabilitationLengthRule pwb007 =
            new Pwb007MvRehabilitationLengthRule();

    private final Pwb008LvRehabilitationLengthRule pwb008 =
            new Pwb008LvRehabilitationLengthRule();

    private final Pwb009NonNegativeLoadShedRule pwb009 =
            new Pwb009NonNegativeLoadShedRule();

    @Test
    void pwb001_positiveValue_passes() {
        RuleEvaluation result = pwb001.evaluate(
                context(Map.of("B001", observation("B001", "10"))));

        assertEquals(QualityFindingStatus.PASSED, result.status());
    }

    @Test
    void pwb001_zeroValue_passes() {
        RuleEvaluation result = pwb001.evaluate(
                context(Map.of("B001", observation("B001", "0"))));

        assertEquals(QualityFindingStatus.PASSED, result.status());
    }

    @Test
    void pwb001_negativeValue_fails() {
        RuleEvaluation result = pwb001.evaluate(
                context(Map.of("B001", observation("B001", "-1"))));

        assertEquals(QualityFindingStatus.FAILED, result.status());
        assertTrue(result.title().contains("Negative"));
    }

    @Test
    void pwb001_missingObservation_isNotEvaluable() {
        RuleEvaluation result = pwb001.evaluate(
                context(Map.of()));

        assertEquals(QualityFindingStatus.NOT_EVALUABLE, result.status());
    }

    @Test
    void pwb002_exactReconciliation_passes() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B001", observation("B001", "100"));
        observations.put("B002", observation("B002", "25"));
        observations.put("B003", observation("B003", "25"));
        observations.put("B004", observation("B004", "25"));
        observations.put("B005", observation("B005", "25"));

        RuleEvaluation result = pwb002.evaluate(context(observations));

        assertEquals(QualityFindingStatus.PASSED, result.status());
    }

    @Test
    void pwb002DifferenceWithinTolerance_passes() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B001", observation("B001", "100.004"));
        observations.put("B002", observation("B002", "25"));
        observations.put("B003", observation("B003", "25"));
        observations.put("B004", observation("B004", "25"));
        observations.put("B005", observation("B005", "25"));

        RuleEvaluation result = pwb002.evaluate(context(observations));

        assertEquals(QualityFindingStatus.PASSED, result.status());
    }

    @Test
    void pwb002DifferenceOutsideTolerance_fails() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B001", observation("B001", "101"));
        observations.put("B002", observation("B002", "25"));
        observations.put("B003", observation("B003", "25"));
        observations.put("B004", observation("B004", "25"));
        observations.put("B005", observation("B005", "25"));

        RuleEvaluation result = pwb002.evaluate(context(observations));

        assertEquals(QualityFindingStatus.FAILED, result.status());
    }

    @Test
    void pwb002MissingComponent_isNotEvaluable() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B001", observation("B001", "100"));
        observations.put("B002", observation("B002", "25"));
        observations.put("B003", observation("B003", "25"));
        observations.put("B004", observation("B004", "25"));

        RuleEvaluation result = pwb002.evaluate(context(observations));

        assertEquals(QualityFindingStatus.NOT_EVALUABLE, result.status());
    }

    @Test
    void pwb003OperationalCapacityBelowInstalledCapacity_passes() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B001", observation("B001", "100"));
        observations.put("B006", observation("B006", "80"));

        RuleEvaluation result = pwb003.evaluate(context(observations));

        assertEquals(QualityFindingStatus.PASSED, result.status());
    }

    @Test
    void pwb003OperationalCapacityEqualInstalledCapacity_passes() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B001", observation("B001", "100"));
        observations.put("B006", observation("B006", "100"));

        RuleEvaluation result = pwb003.evaluate(context(observations));

        assertEquals(QualityFindingStatus.PASSED, result.status());
    }

    @Test
    void pwb003OperationalCapacityAboveInstalledCapacity_fails() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B001", observation("B001", "100"));
        observations.put("B006", observation("B006", "101"));

        RuleEvaluation result = pwb003.evaluate(context(observations));

        assertEquals(QualityFindingStatus.FAILED, result.status());
        assertTrue(result.title().contains("exceeds"));
    }

    @Test
    void pwb003MissingInstalledCapacity_isNotEvaluable() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B006", observation("B006", "80"));

        RuleEvaluation result = pwb003.evaluate(context(observations));

        assertEquals(QualityFindingStatus.NOT_EVALUABLE, result.status());
    }

    @Test
    void pwb003MissingOperationalCapacity_isNotEvaluable() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B001", observation("B001", "100"));

        RuleEvaluation result = pwb003.evaluate(context(observations));

        assertEquals(QualityFindingStatus.NOT_EVALUABLE, result.status());
    }

    @Test
    void pwb004OperationalCapacityBelowRatedCapacity_passes() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B007", observation("B007", "100"));
        observations.put("B008", observation("B008", "80"));

        RuleEvaluation result = pwb004.evaluate(context(observations));

        assertEquals(QualityFindingStatus.PASSED, result.status());
    }

    @Test
    void pwb004OperationalCapacityEqualRatedCapacity_passes() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B007", observation("B007", "100"));
        observations.put("B008", observation("B008", "100"));

        RuleEvaluation result = pwb004.evaluate(context(observations));

        assertEquals(QualityFindingStatus.PASSED, result.status());
    }

    @Test
    void pwb004OperationalCapacityAboveRatedCapacity_fails() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B007", observation("B007", "100"));
        observations.put("B008", observation("B008", "101"));

        RuleEvaluation result = pwb004.evaluate(context(observations));

        assertEquals(QualityFindingStatus.FAILED, result.status());
        assertTrue(result.title().contains("exceeds"));
    }

    @Test
    void pwb004MissingRatedCapacity_isNotEvaluable() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B008", observation("B008", "80"));

        RuleEvaluation result = pwb004.evaluate(context(observations));

        assertEquals(QualityFindingStatus.NOT_EVALUABLE, result.status());
    }

    @Test
    void pwb004MissingOperationalCapacity_isNotEvaluable() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B007", observation("B007", "100"));

        RuleEvaluation result = pwb004.evaluate(context(observations));

        assertEquals(QualityFindingStatus.NOT_EVALUABLE, result.status());
    }

    @Test
    void pwb005RehabilitationLengthBelowTotalLength_passes() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B024", observation("B024", "1000"));
        observations.put("B025", observation("B025", "300"));

        RuleEvaluation result = pwb005.evaluate(context(observations));

        assertEquals(QualityFindingStatus.PASSED, result.status());
    }

    @Test
    void pwb005RehabilitationLengthEqualTotalLength_passes() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B024", observation("B024", "1000"));
        observations.put("B025", observation("B025", "1000"));

        RuleEvaluation result = pwb005.evaluate(context(observations));

        assertEquals(QualityFindingStatus.PASSED, result.status());
    }

    @Test
    void pwb005RehabilitationLengthAboveTotalLength_fails() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B024", observation("B024", "1000"));
        observations.put("B025", observation("B025", "1001"));

        RuleEvaluation result = pwb005.evaluate(context(observations));

        assertEquals(QualityFindingStatus.FAILED, result.status());
        assertTrue(result.title().contains("exceeds"));
    }

    @Test
    void pwb005MissingTotalLength_isNotEvaluable() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B025", observation("B025", "300"));

        RuleEvaluation result = pwb005.evaluate(context(observations));

        assertEquals(QualityFindingStatus.NOT_EVALUABLE, result.status());
    }

    @Test
    void pwb005MissingRehabilitationLength_isNotEvaluable() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B024", observation("B024", "1000"));

        RuleEvaluation result = pwb005.evaluate(context(observations));

        assertEquals(QualityFindingStatus.NOT_EVALUABLE, result.status());
    }

    @Test
    void pwb006ExactReconciliation_passes() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B013", observation("B013", "100"));
        observations.put("B014", observation("B014", "25"));
        observations.put("B015", observation("B015", "25"));
        observations.put("B016", observation("B016", "25"));
        observations.put("B017", observation("B017", "25"));

        RuleEvaluation result = pwb006.evaluate(context(observations));

        assertEquals(QualityFindingStatus.PASSED, result.status());
    }

    @Test
    void pwb006DifferenceWithinTolerance_passes() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B013", observation("B013", "100.004"));
        observations.put("B014", observation("B014", "25"));
        observations.put("B015", observation("B015", "25"));
        observations.put("B016", observation("B016", "25"));
        observations.put("B017", observation("B017", "25"));

        RuleEvaluation result = pwb006.evaluate(context(observations));

        assertEquals(QualityFindingStatus.PASSED, result.status());
    }

    @Test
    void pwb006DifferenceOutsideTolerance_fails() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B013", observation("B013", "101"));
        observations.put("B014", observation("B014", "25"));
        observations.put("B015", observation("B015", "25"));
        observations.put("B016", observation("B016", "25"));
        observations.put("B017", observation("B017", "25"));

        RuleEvaluation result = pwb006.evaluate(context(observations));

        assertEquals(QualityFindingStatus.FAILED, result.status());
    }

    @Test
    void pwb006MissingComponent_isNotEvaluable() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B013", observation("B013", "100"));
        observations.put("B014", observation("B014", "25"));
        observations.put("B015", observation("B015", "25"));
        observations.put("B016", observation("B016", "25"));

        RuleEvaluation result = pwb006.evaluate(context(observations));

        assertEquals(QualityFindingStatus.NOT_EVALUABLE, result.status());
    }

    @Test
    void pwb006MissingTotal_isNotEvaluable() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B014", observation("B014", "25"));
        observations.put("B015", observation("B015", "25"));
        observations.put("B016", observation("B016", "25"));
        observations.put("B017", observation("B017", "25"));

        RuleEvaluation result = pwb006.evaluate(context(observations));

        assertEquals(QualityFindingStatus.NOT_EVALUABLE, result.status());
    }

    @Test
    void pwb007RehabilitationLengthBelowTotalLength_passes() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B026", observation("B026", "1000"));
        observations.put("B027", observation("B027", "300"));

        RuleEvaluation result = pwb007.evaluate(context(observations));

        assertEquals(QualityFindingStatus.PASSED, result.status());
    }

    @Test
    void pwb007RehabilitationLengthEqualTotalLength_passes() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B026", observation("B026", "1000"));
        observations.put("B027", observation("B027", "1000"));

        RuleEvaluation result = pwb007.evaluate(context(observations));

        assertEquals(QualityFindingStatus.PASSED, result.status());
    }

    @Test
    void pwb007RehabilitationLengthAboveTotalLength_fails() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B026", observation("B026", "1000"));
        observations.put("B027", observation("B027", "1001"));

        RuleEvaluation result = pwb007.evaluate(context(observations));

        assertEquals(QualityFindingStatus.FAILED, result.status());
        assertTrue(result.title().contains("exceeds"));
    }

    @Test
    void pwb007MissingTotalLength_isNotEvaluable() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B027", observation("B027", "300"));

        RuleEvaluation result = pwb007.evaluate(context(observations));

        assertEquals(QualityFindingStatus.NOT_EVALUABLE, result.status());
    }

    @Test
    void pwb007MissingRehabilitationLength_isNotEvaluable() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B026", observation("B026", "1000"));

        RuleEvaluation result = pwb007.evaluate(context(observations));

        assertEquals(QualityFindingStatus.NOT_EVALUABLE, result.status());
    }

    @Test
    void pwb008RehabilitationLengthBelowTotalLength_passes() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B028", observation("B028", "1000"));
        observations.put("B029", observation("B029", "300"));

        RuleEvaluation result = pwb008.evaluate(context(observations));

        assertEquals(QualityFindingStatus.PASSED, result.status());
    }

    @Test
    void pwb008RehabilitationLengthEqualTotalLength_passes() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B028", observation("B028", "1000"));
        observations.put("B029", observation("B029", "1000"));

        RuleEvaluation result = pwb008.evaluate(context(observations));

        assertEquals(QualityFindingStatus.PASSED, result.status());
    }

    @Test
    void pwb008RehabilitationLengthAboveTotalLength_fails() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B028", observation("B028", "1000"));
        observations.put("B029", observation("B029", "1001"));

        RuleEvaluation result = pwb008.evaluate(context(observations));

        assertEquals(QualityFindingStatus.FAILED, result.status());
        assertTrue(result.title().contains("exceeds"));
    }

    @Test
    void pwb008MissingTotalLength_isNotEvaluable() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B029", observation("B029", "300"));

        RuleEvaluation result = pwb008.evaluate(context(observations));

        assertEquals(QualityFindingStatus.NOT_EVALUABLE, result.status());
    }

    @Test
    void pwb008MissingRehabilitationLength_isNotEvaluable() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B028", observation("B028", "1000"));

        RuleEvaluation result = pwb008.evaluate(context(observations));

        assertEquals(QualityFindingStatus.NOT_EVALUABLE, result.status());
    }

    @Test
    void pwb009PositiveLoadShed_passes() {
        RuleEvaluation result = pwb009.evaluate(
                context(Map.of("B034", observation("B034", "10"))));

        assertEquals(QualityFindingStatus.PASSED, result.status());
    }

    @Test
    void pwb009ZeroLoadShed_passes() {
        RuleEvaluation result = pwb009.evaluate(
                context(Map.of("B034", observation("B034", "0"))));

        assertEquals(QualityFindingStatus.PASSED, result.status());
    }

    @Test
    void pwb009NegativeLoadShed_fails() {
        RuleEvaluation result = pwb009.evaluate(
                context(Map.of("B034", observation("B034", "-1"))));

        assertEquals(QualityFindingStatus.FAILED, result.status());
        assertTrue(result.title().contains("negative"));
    }

    @Test
    void pwb009MissingObservation_isNotEvaluable() {
        RuleEvaluation result = pwb009.evaluate(
                context(Map.of()));

        assertEquals(QualityFindingStatus.NOT_EVALUABLE, result.status());
    }

    @Test
    void pwb009MissingReferenceYear_isNotEvaluable() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        observations.put("B034", observation("B034", "10"));

        // The existing fixture always supplies a reference year, so this
        // scenario is covered by the rule's explicit reference-year guard
        // through an empty context.
        RuleEvaluation result = pwb009.evaluate(
                new QualityEvaluationContext(
                        new HashMap<>(),
                        new HashMap<>()));

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
