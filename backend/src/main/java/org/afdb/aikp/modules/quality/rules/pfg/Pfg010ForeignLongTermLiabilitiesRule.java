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
public class Pfg010ForeignLongTermLiabilitiesRule implements QualityRule {

    @Override
    public String code() {
        return "FG-010";
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
        return context.variable("F178").isPresent()
                && context.variable("F179").isPresent();
    }

    @Override
    public RuleEvaluation evaluate(QualityEvaluationContext context) {

        if (context.referenceYears().isEmpty()) {
            return RuleEvaluation.notEvaluable(
                code(),
                type(),
                provenance(),
                severity(),
                "Foreign long-term liabilities cannot be evaluated.",
                "No reference year is available for F178/F179.",
                null,
                List.of("F178", "F179"),
                Map.of());
        }

        int year = context.latestReferenceYear();

        var longTermLiabilities = context.numericValue("F178", year);
        var foreignLongTermLiabilities = context.numericValue("F179", year);

        if (longTermLiabilities.isEmpty() || foreignLongTermLiabilities.isEmpty()) {
            return RuleEvaluation.notEvaluable(
                code(),
                type(),
                provenance(),
                severity(),
                "Foreign long-term liabilities cannot be evaluated.",
                "F178 or F179 is missing for the evaluated reference year.",
                year,
                List.of("F178", "F179"),
                Map.of());
        }

        BigDecimal f178 = longTermLiabilities.get();
        BigDecimal f179 = foreignLongTermLiabilities.get();

        if (f179.compareTo(f178) <= 0) {
            return RuleEvaluation.passed(
                code(),
                type(),
                provenance(),
                severity(),
                "F179 does not exceed F178.",
                year,
                List.of("F178", "F179"),
                Map.of(
                    "longTermLiabilitiesF178", f178,
                    "foreignLongTermLiabilitiesF179", f179));
        }

        return RuleEvaluation.failed(
            code(),
            type(),
            provenance(),
            severity(),
            "F179 exceeds F178.",
            "Foreign long-term liabilities exceed total long-term liabilities.",
            "F179 should not exceed F178.",
            "Review the reported long-term liabilities and their foreign component.",
            year,
            List.of("F178", "F179"),
            Map.of(
                "longTermLiabilitiesF178", f178,
                "foreignLongTermLiabilitiesF179", f179,
                "excess", f179.subtract(f178)));
    }
}
