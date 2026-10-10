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
public class Pfg008AccumulatedPpeDepreciationRule implements QualityRule {

    @Override
    public String code() {
        return "FG-008";
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
        return context.variable("F173").isPresent()
                && context.variable("F174").isPresent();
    }

    @Override
    public RuleEvaluation evaluate(QualityEvaluationContext context) {

        if (context.referenceYears().isEmpty()) {
            return RuleEvaluation.notEvaluable(
                code(),
                type(),
                provenance(),
                severity(),
                "Accumulated PPE depreciation cannot be evaluated.",
                "No reference year is available for F173/F174.",
                null,
                List.of("F173", "F174"),
                Map.of());
        }

        int year = context.latestReferenceYear();

        var grossPpeValue = context.numericValue("F173", year);
        var accumulatedDepreciation = context.numericValue("F174", year);

        if (grossPpeValue.isEmpty() || accumulatedDepreciation.isEmpty()) {
            return RuleEvaluation.notEvaluable(
                code(),
                type(),
                provenance(),
                severity(),
                "Accumulated PPE depreciation cannot be evaluated.",
                "F173 or F174 is missing for the evaluated reference year.",
                year,
                List.of("F173", "F174"),
                Map.of());
        }

        BigDecimal f173 = grossPpeValue.get();
        BigDecimal f174 = accumulatedDepreciation.get();

        if (f174.compareTo(f173) <= 0) {
            return RuleEvaluation.passed(
                code(),
                type(),
                provenance(),
                severity(),
                "F174 does not exceed F173.",
                year,
                List.of("F173", "F174"),
                Map.of(
                    "grossPpeValueF173", f173,
                    "accumulatedDepreciationF174", f174));
        }

        return RuleEvaluation.failed(
            code(),
            type(),
            provenance(),
            severity(),
            "F174 exceeds F173.",
            "Accumulated depreciation on property, plant, and equipment exceeds the gross value of property, plant, and equipment.",
            "F174 should not exceed F173.",
            "Review the reported PPE gross value and accumulated depreciation.",
            year,
            List.of("F173", "F174"),
            Map.of(
                "grossPpeValueF173", f173,
                "accumulatedDepreciationF174", f174,
                "excess", f174.subtract(f173)));
    }
}
