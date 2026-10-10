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
public final class Pwb009NonNegativeLoadShedRule implements QualityRule {

    @Override
    public String code() {
        return "PWB-009";
    }

    @Override
    public QualityRuleType type() {
        return QualityRuleType.STRUCTURAL;
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
        return context.variable("B034").isPresent();
    }

    @Override
    public RuleEvaluation evaluate(QualityEvaluationContext context) {
        if (context.referenceYears().isEmpty()) {
            return RuleEvaluation.notEvaluable(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "Load shed cannot be evaluated.",
                    "No reference year is available for B034.",
                    null,
                    List.of("B034"),
                    Map.of());
        }

        int year = context.latestReferenceYear();

        var b034 = context.numericValue("B034", year);

        if (b034.isEmpty()) {
            return RuleEvaluation.notEvaluable(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "Load shed cannot be evaluated.",
                    "B034 is missing or not provided for the evaluated reference year.",
                    year,
                    List.of("B034"),
                    Map.of());
        }

        BigDecimal loadShed = b034.get();

        Map<String, Object> evidence = Map.of(
                "reportedB034", loadShed);

        if (loadShed.compareTo(BigDecimal.ZERO) >= 0) {
            return RuleEvaluation.passed(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "Load shed is non-negative.",
                    year,
                    List.of("B034"),
                    evidence);
        }

        return RuleEvaluation.failed(
                code(),
                type(),
                provenance(),
                severity(),
                "Load shed has a negative value.",
                "B034 cannot be negative.",
                "The reported load shed value is below zero.",
                "Review B034 and verify the reported load shed value for the reference year.",
                year,
                List.of("B034"),
                evidence);
    }
}
