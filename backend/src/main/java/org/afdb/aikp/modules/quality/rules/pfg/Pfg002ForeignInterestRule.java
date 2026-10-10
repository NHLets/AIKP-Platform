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
public final class Pfg002ForeignInterestRule implements QualityRule {

    @Override
    public String code() {
        return "FG-002";
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
        return context.variable("F141").isPresent()
                && context.variable("F142").isPresent();
    }

    @Override
    public RuleEvaluation evaluate(QualityEvaluationContext context) {
        if (context.referenceYears().isEmpty()) {
            return RuleEvaluation.notEvaluable(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "Foreign interest paid cannot be evaluated.",
                    "No reference year is available for F141 and F142.",
                    null,
                    List.of("F141", "F142"),
                    Map.of()
            );
        }

        int year = context.latestReferenceYear();

        var f141 = context.numericValue("F141", year);
        var f142 = context.numericValue("F142", year);

        if (f141.isEmpty() || f142.isEmpty()) {
            return RuleEvaluation.notEvaluable(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "Foreign interest paid cannot be evaluated.",
                    "F141 and F142 must both be available for the evaluated reference year.",
                    year,
                    List.of("F141", "F142"),
                    Map.of()
            );
        }

        BigDecimal totalInterest = f141.get();
        BigDecimal foreignInterest = f142.get();

        Map<String, Object> evidence = Map.of(
                "totalInterestPaidF141", totalInterest,
                "foreignInterestPaidF142", foreignInterest
        );

        if (foreignInterest.compareTo(totalInterest) <= 0) {
            return RuleEvaluation.passed(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "F142 is consistent with F141.",
                    year,
                    List.of("F141", "F142"),
                    evidence
            );
        }

        return RuleEvaluation.failed(
                code(),
                type(),
                provenance(),
                severity(),
                "F142 exceeds F141.",
                "Foreign interest paid cannot exceed total interest paid.",
                "F142 should be less than or equal to F141 because F142 represents the foreign component of F141.",
                "Review F141 and F142 and verify the reported interest expenditure breakdown.",
                year,
                List.of("F141", "F142"),
                evidence
        );
    }
}
