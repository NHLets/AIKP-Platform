package org.afdb.aikp.modules.quality.rules.pwb;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import org.afdb.aikp.modules.quality.domain.enums.*;
import org.afdb.aikp.modules.quality.domain.model.RuleEvaluation;
import org.afdb.aikp.modules.quality.domain.service.QualityEvaluationContext;
import org.afdb.aikp.modules.quality.domain.service.QualityRule;
import org.afdb.aikp.modules.quality.domain.service.QualityRuleSupport;

@Component
public final class Pwb003OperationalCapacityRule
        implements QualityRule {

    public String code() { return "PWB-003"; }
    public QualityRuleType type() { return QualityRuleType.LOGICAL; }
    public QualityRuleProvenance provenance() { return QualityRuleProvenance.PROPOSED; }
    public QualitySeverity severity() { return QualitySeverity.MAJOR; }

    public boolean appliesTo(QualityEvaluationContext context) {
        return context.variable("B006").isPresent();
    }

    public RuleEvaluation evaluate(QualityEvaluationContext context) {
        int year = context.latestReferenceYear();

        var b001 = context.numericValue("B001", year);
        var b006 = context.numericValue("B006", year);

        if (b001.isEmpty() || b006.isEmpty()) {
            return RuleEvaluation.notEvaluable(
                    code(), type(), provenance(), severity(),
                    "Operational generation capacity cannot be evaluated.",
                    "B001 and B006 must both be provided for the evaluated reference year.",
                    year,
                    List.of("B001", "B006"),
                    Map.of());
        }

        BigDecimal installedCapacity = b001.get();
        BigDecimal operationalCapacity = b006.get();

        Map<String, Object> evidence = Map.of(
                "installedCapacityMW", installedCapacity,
                "operationalCapacityMW", operationalCapacity,
                "differenceMW", operationalCapacity.subtract(installedCapacity));

        if (operationalCapacity.compareTo(installedCapacity) <= 0) {
            return RuleEvaluation.passed(
                    code(), type(), provenance(), severity(),
                    "Operational generation capacity is consistent with installed generation capacity.",
                    year,
                    List.of("B001", "B006"),
                    evidence);
        }

        return RuleEvaluation.failed(
                code(), type(), provenance(), severity(),
                "Operational generation capacity exceeds installed generation capacity.",
                "B006 is greater than B001.",
                "The reported operational generation capacity exceeds the reported installed generation capacity.",
                "Review the reported values for B001 and B006.",
                year,
                List.of("B001", "B006"),
                evidence);
    }
}
