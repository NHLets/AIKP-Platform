package org.afdb.aikp.modules.quality.rules.pwa;

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

class PwaRulesTest {

    private static final int YEAR = 2025;

    private final Pwa001CompletenessRule pwa001 =
            new Pwa001CompletenessRule();
    private final Pwa002RuralElectrificationJurisdictionRule pwa002 =
            new Pwa002RuralElectrificationJurisdictionRule();
    private final Pwa003MarketModelRule pwa003 =
            new Pwa003MarketModelRule();
    private final Pwa004TransmissionTariffRule pwa004 =
            new Pwa004TransmissionTariffRule();
    private final Pwa005RuralFundCostRecoveryRule pwa005 =
            new Pwa005RuralFundCostRecoveryRule();
    private final Pwa006QualityStandardsPenaltiesRule pwa006 =
            new Pwa006QualityStandardsPenaltiesRule();

    @Test
    void pwa001_allRequiredObservationsProvided_passes() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        for (String code : requiredCodes()) {
            observations.put(code, observation(code, "1"));
        }

        RuleEvaluation result = pwa001.evaluate(context(observations));

        assertEquals(QualityFindingStatus.PASSED, result.status());
    }

    @Test
    void pwa001_missingRequiredObservation_fails() {
        Map<String, DataCollectionObservation> observations = new HashMap<>();
        for (String code : requiredCodes()) {
            if (!code.equals("D040")) {
                observations.put(code, observation(code, "1"));
            }
        }

        RuleEvaluation result = pwa001.evaluate(context(observations));

        assertEquals(QualityFindingStatus.FAILED, result.status());
        assertTrue(result.evidence().toString().contains("D040"));
    }

    @Test
    void pwa001_noReferenceYear_isNotEvaluable() {
        RuleEvaluation result = pwa001.evaluate(context(Map.of()));

        assertEquals(QualityFindingStatus.NOT_EVALUABLE, result.status());
    }

    @Test
    void pwa002_allowedCodes_pass() {
        for (String value : List.of("0", "1", "2")) {
            RuleEvaluation result =
                    pwa002.evaluate(context(Map.of("D007", observation("D007", value))));
            assertEquals(QualityFindingStatus.PASSED, result.status());
        }
    }

    @Test
    void pwa002_invalidCode_fails() {
        RuleEvaluation result =
                pwa002.evaluate(context(Map.of("D007", observation("D007", "3"))));

        assertEquals(QualityFindingStatus.FAILED, result.status());
    }

    @Test
    void pwa002_missingValue_isNotEvaluable() {
        RuleEvaluation result = pwa002.evaluate(context(Map.of()));

        assertEquals(QualityFindingStatus.NOT_EVALUABLE, result.status());
    }

    @Test
    void pwa003_allowedCodes_pass() {
        for (String value : List.of("0", "1", "2", "3")) {
            RuleEvaluation result =
                    pwa003.evaluate(context(Map.of("D012", observation("D012", value))));
            assertEquals(QualityFindingStatus.PASSED, result.status());
        }
    }

    @Test
    void pwa003_invalidCode_fails() {
        RuleEvaluation result =
                pwa003.evaluate(context(Map.of("D012", observation("D012", "4"))));

        assertEquals(QualityFindingStatus.FAILED, result.status());
    }

    @Test
    void pwa004_allowedCodes_pass() {
        for (String value : List.of("0", "1", "2", "3")) {
            RuleEvaluation result =
                    pwa004.evaluate(context(Map.of("D031", observation("D031", value))));
            assertEquals(QualityFindingStatus.PASSED, result.status());
        }
    }

    @Test
    void pwa004_invalidCode_fails() {
        RuleEvaluation result =
                pwa004.evaluate(context(Map.of("D031", observation("D031", "4"))));

        assertEquals(QualityFindingStatus.FAILED, result.status());
    }

    @Test
    void pwa005_allowedCodes_pass() {
        for (String value : List.of("0", "1", "2", "3")) {
            RuleEvaluation result =
                    pwa005.evaluate(context(Map.of("D040", observation("D040", value))));
            assertEquals(QualityFindingStatus.PASSED, result.status());
        }
    }

    @Test
    void pwa005_invalidCode_fails() {
        RuleEvaluation result =
                pwa005.evaluate(context(Map.of("D040", observation("D040", "4"))));

        assertEquals(QualityFindingStatus.FAILED, result.status());
    }

    @Test
    void pwa006_standardsAndPenaltiesBothTrue_passes() {
        RuleEvaluation result = pwa006.evaluate(
                context(Map.of(
                        "D037", booleanObservation("D037", true),
                        "D038", booleanObservation("D038", true))));

        assertEquals(QualityFindingStatus.PASSED, result.status());
    }

    @Test
    void pwa006_standardsAndPenaltiesBothFalse_passes() {
        RuleEvaluation result = pwa006.evaluate(
                context(Map.of(
                        "D037", booleanObservation("D037", false),
                        "D038", booleanObservation("D038", false))));

        assertEquals(QualityFindingStatus.PASSED, result.status());
    }

    @Test
    void pwa006_penaltiesTrueStandardsFalse_fails() {
        RuleEvaluation result = pwa006.evaluate(
                context(Map.of(
                        "D037", booleanObservation("D037", false),
                        "D038", booleanObservation("D038", true))));

        assertEquals(QualityFindingStatus.FAILED, result.status());
        assertTrue(result.title().contains("D038"));
    }

    @Test
    void pwa006_missingBooleanValue_isNotEvaluable() {
        RuleEvaluation result = pwa006.evaluate(
                context(Map.of(
                        "D037", booleanObservation("D037", true))));

        assertEquals(QualityFindingStatus.NOT_EVALUABLE, result.status());
    }

    private static List<String> requiredCodes() {
        return List.of(
                "D001", "D002", "D003", "D004", "D005", "D006",
                "D007", "D011", "D012", "D017", "D021", "D025",
                "D029", "D030", "D031", "D036", "D037", "D038",
                "D039", "D040", "D044");
    }

    private static QualityEvaluationContext context(
            Map<String, DataCollectionObservation> observations) {

        QuestionnaireId questionnaireId = QuestionnaireId.generate();

        Map<String, QuestionnaireVariable> variables = new HashMap<>();
        Map<String, List<DataCollectionObservation>> grouped =
                new HashMap<>();

        for (Map.Entry<String, DataCollectionObservation> entry
                : observations.entrySet()) {

            String code = entry.getKey().toUpperCase();
            DataCollectionObservation observation = entry.getValue();

            QuestionnaireVariableDataType dataType =
                    observation.getBooleanValue() != null
                            ? QuestionnaireVariableDataType.BOOLEAN
                            : QuestionnaireVariableDataType.NUMBER;

            variables.put(
                    code,
                    QuestionnaireVariable.create(
                            questionnaireId,
                            null,
                            code,
                            code,
                            code,
                            dataType,
                            null,
                            true,
                            1));

            grouped.put(code, List.of(observation));
        }

        // PWA-001 requires all 21 variables to exist in metadata,
        // even when an observation is intentionally missing.
        for (String code : requiredCodes()) {
            variables.putIfAbsent(
                    code,
                    QuestionnaireVariable.create(
                            questionnaireId,
                            null,
                            code,
                            code,
                            code,
                            booleanCode(code)
                                    ? QuestionnaireVariableDataType.BOOLEAN
                                    : QuestionnaireVariableDataType.NUMBER,
                            null,
                            true,
                            1));
        }

        return new QualityEvaluationContext(variables, grouped);
    }

    private static boolean booleanCode(String code) {
        return code.equals("D001")
                || code.equals("D002")
                || code.equals("D003")
                || code.equals("D004")
                || code.equals("D005")
                || code.equals("D006")
                || code.equals("D011")
                || code.equals("D030")
                || code.equals("D036")
                || code.equals("D037")
                || code.equals("D038")
                || code.equals("D039")
                || code.equals("D044");
    }

    private static DataCollectionObservation observation(
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
                null,
                null);
    }

    private static DataCollectionObservation booleanObservation(
            String code,
            boolean value) {

        return DataCollectionObservation.provided(
                DataCollectionObservationId.generate(),
                DataCollectionId.generate(),
                QuestionnaireVariableId.generate(),
                YEAR,
                QuestionnaireVariableDataType.BOOLEAN,
                null,
                null,
                value,
                null,
                null,
                null);
    }
}
