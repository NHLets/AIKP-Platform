package org.afdb.aikp.modules.quality.rules.pfg;

import org.afdb.aikp.modules.quality.domain.enums.QualityRuleProvenance;
import org.afdb.aikp.modules.quality.domain.enums.QualityRuleType;
import org.afdb.aikp.modules.quality.domain.enums.QualitySeverity;
import org.afdb.aikp.modules.quality.domain.model.RuleEvaluation;
import org.afdb.aikp.modules.quality.domain.service.QualityRuleSupport;
import org.afdb.aikp.modules.quality.domain.service.QualityEvaluationContext;
import org.afdb.aikp.modules.quality.domain.service.QualityRule;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
public class Pfg007AccumulatedRehabilitationDepreciationRule implements QualityRule {

    @Override
    public String code() {
        return "FG-007";
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
        return context.variable("F171").isPresent()
                && context.variable("F172").isPresent();
    }

    @Override
    public RuleEvaluation evaluate(QualityEvaluationContext context) {

        if (context.referenceYears().isEmpty()) {
            return RuleEvaluation.notEvaluable(
                code(),
                type(),
                provenance(),
                severity(),
                "Accumulated rehabilitation depreciation cannot be evaluated.",
                "No reference year is available for F171/F172.",
                null,
                List.of("F171", "F172"),
                Map.of());
        }

        int year = context.latestReferenceYear();

        var grossValue = context.numericValue("F171", year);
        var accumulatedDepreciation = context.numericValue("F172", year);

        if (grossValue.isEmpty() || accumulatedDepreciation.isEmpty()) {
            return RuleEvaluation.notEvaluable(
                code(),
                type(),
                provenance(),
                severity(),
                "Accumulated rehabilitation depreciation cannot be evaluated.",
                "F171 or F172 is missing for the evaluated reference year.",
                year,
                List.of("F171", "F172"),
                Map.of());
        }

        BigDecimal f171 = grossValue.get();
        BigDecimal f172 = accumulatedDepreciation.get();

        if (f172.compareTo(f171) <= 0) {
            return RuleEvaluation.passed(
                code(),
                type(),
                provenance(),
                severity(),
                "F172 does not exceed F171.",
                year,
                List.of("F171", "F172"),
                Map.of(
                    "grossRehabilitationValueF171", f171,
                    "accumulatedDepreciationF172", f172));
        }

        return RuleEvaluation.failed(
            code(),
            type(),
            provenance(),
            severity(),
            "F172 exceeds F171.",
            "Accumulated depreciation on deferred rehabilitation costs exceeds the gross value of capitalized rehabilitation costs.",
            "F172 should not exceed F171.",
            "Review the reported rehabilitation asset values and accumulated depreciation.",
            year,
            List.of("F171", "F172"),
            Map.of(
                "grossRehabilitationValueF171", f171,
                "accumulatedDepreciationF172", f172,
                "excess", f172.subtract(f171)));
    }
}
