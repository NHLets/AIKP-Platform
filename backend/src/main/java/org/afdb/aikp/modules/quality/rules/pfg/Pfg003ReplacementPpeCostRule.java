package org.afdb.aikp.modules.quality.rules.pfg;

import org.afdb.aikp.modules.quality.domain.enums.QualityRuleProvenance;
import org.afdb.aikp.modules.quality.domain.enums.QualityRuleType;
import org.afdb.aikp.modules.quality.domain.enums.QualitySeverity;
import org.afdb.aikp.modules.quality.domain.model.RuleEvaluation;
import org.afdb.aikp.modules.quality.domain.service.QualityEvaluationContext;
import org.afdb.aikp.modules.quality.domain.service.QualityRule;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
public final class Pfg003ReplacementPpeCostRule implements QualityRule {

    @Override
    public String code() {
        return "FG-003";
    }

    @Override
    public QualityRuleType type() {
        return QualityRuleType.LOGICAL;
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
        return context.variable("F160").isPresent()
                && context.variable("F161").isPresent();
    }

    @Override
    public RuleEvaluation evaluate(QualityEvaluationContext context) {
        if (context.referenceYears().isEmpty()) {
            return RuleEvaluation.notEvaluable(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "Replacement PPE cost cannot be evaluated.",
                    "No reference year is available for F160 and F161.",
                    null,
                    List.of("F160", "F161"),
                    Map.of()
            );
        }

        int year = context.latestReferenceYear();

        var f160 = context.numericValue("F160", year);
        var f161 = context.numericValue("F161", year);

        if (f160.isEmpty() || f161.isEmpty()) {
            return RuleEvaluation.notEvaluable(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "Replacement PPE cost cannot be evaluated.",
                    "F160 and F161 must both be available for the evaluated reference year.",
                    year,
                    List.of("F160", "F161"),
                    Map.of()
            );
        }

        BigDecimal totalPpePurchases = f160.get();
        BigDecimal replacementPpePurchases = f161.get();

        Map<String, Object> evidence = Map.of(
                "purchaseOfPpeF160", totalPpePurchases,
                "replacementPpeF161", replacementPpePurchases
        );

        if (replacementPpePurchases.compareTo(totalPpePurchases) <= 0) {
            return RuleEvaluation.passed(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "F161 is consistent with F160.",
                    year,
                    List.of("F160", "F161"),
                    evidence
            );
        }

        return RuleEvaluation.failed(
                code(),
                type(),
                provenance(),
                severity(),
                "F161 exceeds F160.",
                "Replacement property, plant and equipment costs cannot exceed total purchases of property, plant and equipment.",
                "F161 should be less than or equal to F160 because F161 is explicitly identified as a component of F160.",
                "Review F160 and F161 and verify the reported PPE expenditure breakdown.",
                year,
                List.of("F160", "F161"),
                evidence
        );
    }
}
