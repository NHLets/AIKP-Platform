package org.afdb.aikp.modules.quality.rules.pwa;

import java.util.List;
import java.util.Map;
import org.afdb.aikp.modules.quality.domain.enums.QualityRuleProvenance;
import org.afdb.aikp.modules.quality.domain.enums.QualityRuleType;
import org.afdb.aikp.modules.quality.domain.enums.QualitySeverity;
import org.afdb.aikp.modules.quality.domain.model.RuleEvaluation;
import org.afdb.aikp.modules.quality.domain.service.QualityEvaluationContext;
import org.afdb.aikp.modules.quality.domain.service.QualityRule;
import org.springframework.stereotype.Component;

@Component
public final class Pwa001CompletenessRule implements QualityRule {

    private static final List<String> REQUIRED_VARIABLES = List.of(
            "D001", "D002", "D003", "D004", "D005", "D006",
            "D007", "D011", "D012", "D017", "D021", "D025",
            "D029", "D030", "D031", "D036", "D037", "D038",
            "D039", "D040", "D044"
    );

    @Override
    public String code() {
        return "PWA-001";
    }

    @Override
    public QualityRuleType type() {
        return QualityRuleType.STRUCTURAL;
    }

    @Override
    public QualityRuleProvenance provenance() {
        return QualityRuleProvenance.SOURCE_DEFINED;
    }

    @Override
    public QualitySeverity severity() {
        return QualitySeverity.MAJOR;
    }

    @Override
    public boolean appliesTo(QualityEvaluationContext context) {
        return REQUIRED_VARIABLES.stream()
                .allMatch(code -> context.variable(code).isPresent());
    }

    @Override
    public RuleEvaluation evaluate(QualityEvaluationContext context) {

        if (context.referenceYears().isEmpty()) {
            return RuleEvaluation.notEvaluable(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "PW_A completeness cannot be evaluated.",
                    "No reference year is available in the submitted observations.",
                    null,
                    REQUIRED_VARIABLES,
                    Map.of("requiredVariables", REQUIRED_VARIABLES.size())
            );
        }

        int year = context.latestReferenceYear();

        List<String> missing = REQUIRED_VARIABLES.stream()
                .filter(code -> context.observation(code, year).isEmpty())
                .toList();

        List<String> notProvided = REQUIRED_VARIABLES.stream()
                .filter(code -> context.observationStatus(code, year).isPresent()
                        && context.numericValue(code, year).isEmpty()
                        && context.booleanValue(code, year).isEmpty()
                        && context.textValue(code, year).isEmpty())
                .toList();

        if (missing.isEmpty() && notProvided.isEmpty()) {
            return RuleEvaluation.passed(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "PW_A is complete for the evaluated reference year.",
                    year,
                    REQUIRED_VARIABLES,
                    Map.of(
                            "referenceYear", year,
                            "requiredVariables", REQUIRED_VARIABLES.size(),
                            "observedVariables", REQUIRED_VARIABLES.size(),
                            "missingVariables", List.of(),
                            "notProvidedVariables", List.of()
                    )
            );
        }

        List<String> affected = new java.util.ArrayList<>(missing);
        notProvided.stream()
                .filter(code -> !affected.contains(code))
                .forEach(affected::add);

        return RuleEvaluation.failed(
                code(),
                type(),
                provenance(),
                severity(),
                "PW_A is incomplete for the evaluated reference year.",
                "One or more required PW_A variables do not contain a provided observation.",
                "Required variables should have a PROVIDED observation for the evaluated reference year; NOT_APPLICABLE may be used only where the variable is genuinely not applicable.",
                "Review the missing or non-provided PW_A observations and complete the submission where applicable.",
                year,
                affected,
                Map.of(
                        "referenceYear", year,
                        "requiredVariables", REQUIRED_VARIABLES.size(),
                        "missingVariables", missing,
                        "notProvidedVariables", notProvided,
                        "affectedVariables", affected
                )
        );
    }
}
