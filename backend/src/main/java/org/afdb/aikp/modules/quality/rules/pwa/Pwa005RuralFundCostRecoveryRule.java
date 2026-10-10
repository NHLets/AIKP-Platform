package org.afdb.aikp.modules.quality.rules.pwa;

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
public final class Pwa005RuralFundCostRecoveryRule implements QualityRule {

    @Override
    public String code() {
        return "PWA-005";
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
        return context.variable("D040").isPresent();
    }

    @Override
    public RuleEvaluation evaluate(QualityEvaluationContext context) {

        if (context.referenceYears().isEmpty()) {
            return notEvaluable("No reference year is available for D040.", null);
        }

        int year = context.latestReferenceYear();
        var value = context.numericValue("D040", year);

        if (value.isEmpty()) {
            return notEvaluable(
                    "D040 is missing or not provided for the evaluated reference year.",
                    year);
        }

        BigDecimal costRecovery = value.get();

        Map<String, Object> evidence = Map.of(
                "reportedD040", costRecovery,
                "allowedValues", List.of(0, 1, 2, 3)
        );

        if (isInteger(costRecovery)
                && costRecovery.compareTo(BigDecimal.ZERO) >= 0
                && costRecovery.compareTo(BigDecimal.valueOf(3)) <= 0) {

            return RuleEvaluation.passed(
                    code(), type(), provenance(), severity(),
                    "D040 has a valid code.",
                    year,
                    List.of("D040"),
                    evidence
            );
        }

        return RuleEvaluation.failed(
                code(), type(), provenance(), severity(),
                "D040 has an invalid code.",
                "The reported D040 value is outside the coding domain defined by the questionnaire.",
                "D040 must use 0=full subsidy, 1=full capital subsidy, 2=partial capital subsidy, or 3=no subsidy.",
                "Review the D040 value and correct the coding if necessary.",
                year,
                List.of("D040"),
                evidence
        );
    }

    private RuleEvaluation notEvaluable(String explanation, Integer year) {
        return RuleEvaluation.notEvaluable(
                code(), type(), provenance(), severity(),
                "D040 cannot be evaluated.",
                explanation,
                year,
                List.of("D040"),
                Map.of()
        );
    }

    private static boolean isInteger(BigDecimal value) {
        return value.stripTrailingZeros().scale() <= 0;
    }
}
