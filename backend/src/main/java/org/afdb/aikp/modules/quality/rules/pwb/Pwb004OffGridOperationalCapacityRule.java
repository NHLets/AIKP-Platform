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
public final class Pwb004OffGridOperationalCapacityRule
        implements QualityRule {

    public String code() { return "PWB-004"; }
    public QualityRuleType type() { return QualityRuleType.LOGICAL; }
    public QualityRuleProvenance provenance() { return QualityRuleProvenance.PROPOSED; }
    public QualitySeverity severity() { return QualitySeverity.MAJOR; }

    public boolean appliesTo(QualityEvaluationContext context) {
        return context.variable("B008").isPresent();
    }

    public RuleEvaluation evaluate(QualityEvaluationContext context) {
        int year = context.latestReferenceYear();

        var b007 = context.numericValue("B007", year);
        var b008 = context.numericValue("B008", year);

        if (b007.isEmpty() || b008.isEmpty()) {
            return RuleEvaluation.notEvaluable(
                    code(), type(), provenance(), severity(),
                    "Off-grid operational generation capacity cannot be evaluated.",
                    "B007 and B008 must both be provided for the evaluated reference year.",
                    year,
                    List.of("B007", "B008"),
                    Map.of());
        }

        BigDecimal installedCapacity = b007.get();
        BigDecimal operationalCapacity = b008.get();

        Map<String, Object> evidence = Map.of(
                "offGridRatedCapacityMW", installedCapacity,
                "offGridOperationalCapacityMW", operationalCapacity,
                "differenceMW", operationalCapacity.subtract(installedCapacity));

        if (operationalCapacity.compareTo(installedCapacity) <= 0) {
            return RuleEvaluation.passed(
                    code(), type(), provenance(), severity(),
                    "Off-grid operational generation capacity is consistent with rated capacity.",
                    year,
                    List.of("B007", "B008"),
                    evidence);
        }

        return RuleEvaluation.failed(
                code(), type(), provenance(), severity(),
                "Off-grid operational generation capacity exceeds rated capacity.",
                "B008 is greater than B007.",
                "The reported operational capacity of isolated systems exceeds their reported rated capacity.",
                "Review the reported values for B007 and B008.",
                year,
                List.of("B007", "B008"),
                evidence);
    }
}
