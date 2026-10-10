package org.afdb.aikp.modules.quality.rules.pwb;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import org.afdb.aikp.modules.quality.domain.enums.*;
import org.afdb.aikp.modules.quality.domain.model.RuleEvaluation;
import org.afdb.aikp.modules.quality.domain.service.QualityEvaluationContext;
import org.afdb.aikp.modules.quality.domain.service.QualityRule;

@Component
public final class Pwb001NonNegativeGenerationCapacityRule
        implements QualityRule {

    public String code() { return "PWB-001"; }
    public QualityRuleType type() { return QualityRuleType.STRUCTURAL; }
    public QualityRuleProvenance provenance() { return QualityRuleProvenance.PROPOSED; }
    public QualitySeverity severity() { return QualitySeverity.MAJOR; }

    public boolean appliesTo(QualityEvaluationContext context) {
        return context.variable("B001").isPresent();
    }

    public RuleEvaluation evaluate(QualityEvaluationContext context) {
        if (context.referenceYears().isEmpty()) {
            return RuleEvaluation.notEvaluable(
                    code(), type(), provenance(), severity(),
                    "Generation capacity cannot be evaluated.",
                    "B001 has no available observation for a reference year.",
                    null, List.of("B001"), Map.of());
        }

        int year = context.latestReferenceYear();

        var value = context.numericValue("B001", year);
        if (value.isEmpty()) {
            return RuleEvaluation.notEvaluable(
                    code(), type(), provenance(), severity(),
                    "Generation capacity cannot be evaluated.",
                    "B001 is missing or not provided for the evaluated reference year.",
                    year, List.of("B001"), Map.of());
        }

        BigDecimal observed = value.get();

        if (observed.signum() >= 0) {
            return RuleEvaluation.passed(
                    code(), type(), provenance(), severity(),
                    "Generation capacity is non-negative.",
                    context.latestReferenceYear(), List.of("B001"),
                    Map.of("observedValue", observed));
        }

        return RuleEvaluation.failed(
                code(), type(), provenance(), severity(),
                "Negative generation capacity detected.",
                "B001 contains a negative generation capacity.",
                "Generation capacity cannot be negative.",
                "Review the reported generation capacity.",
                context.latestReferenceYear(), List.of("B001"),
                Map.of("observedValue", observed));
    }

}
