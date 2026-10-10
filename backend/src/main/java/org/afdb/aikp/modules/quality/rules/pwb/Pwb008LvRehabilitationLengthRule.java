package org.afdb.aikp.modules.quality.rules.pwb;

import java.math.BigDecimal;
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
public final class Pwb008LvRehabilitationLengthRule implements QualityRule {

    @Override
    public String code() {
        return "PWB-008";
    }

    @Override
    public QualityRuleType type() {
        return QualityRuleType.LOGICAL;
    }

    @Override
    public QualityRuleProvenance provenance() {
        return QualityRuleProvenance.PROPOSED;
    }

    @Override
    public QualitySeverity severity() {
        return QualitySeverity.MAJOR;
    }

    @Override
    public boolean appliesTo(QualityEvaluationContext context) {
        return context.variable("B028").isPresent()
                && context.variable("B029").isPresent();
    }

    @Override
    public RuleEvaluation evaluate(QualityEvaluationContext context) {
        if (context.referenceYears().isEmpty()) {
            return RuleEvaluation.notEvaluable(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "LV network rehabilitation cannot be evaluated.",
                    "No reference year is available for B028 and B029.",
                    null,
                    List.of("B028", "B029"),
                    Map.of());
        }

        int year = context.latestReferenceYear();

        var b028 = context.numericValue("B028", year);
        var b029 = context.numericValue("B029", year);

        if (b028.isEmpty() || b029.isEmpty()) {
            return RuleEvaluation.notEvaluable(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "LV network rehabilitation cannot be evaluated.",
                    "B028 and B029 must both be provided for the evaluated reference year.",
                    year,
                    List.of("B028", "B029"),
                    Map.of());
        }

        BigDecimal totalLvLength = b028.get();
        BigDecimal rehabilitationLvLength = b029.get();

        Map<String, Object> evidence = Map.of(
                "reportedB028", totalLvLength,
                "reportedB029", rehabilitationLvLength);

        if (rehabilitationLvLength.compareTo(totalLvLength) <= 0) {
            return RuleEvaluation.passed(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "LV rehabilitation length is consistent with total LV network length.",
                    year,
                    List.of("B028", "B029"),
                    evidence);
        }

        return RuleEvaluation.failed(
                code(),
                type(),
                provenance(),
                severity(),
                "LV rehabilitation length exceeds total LV network length.",
                "B029 cannot exceed B028.",
                "The reported LV rehabilitation length is greater than the reported total LV network length.",
                "Review B028 and B029 and verify that both values refer to the same network scope and reference year.",
                year,
                List.of("B028", "B029"),
                evidence);
    }
}
