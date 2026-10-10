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
public class Pfg009ForeignCurrentLiabilitiesRule implements QualityRule {

    @Override
    public String code() {
        return "FG-009";
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
        return context.variable("F176").isPresent()
                && context.variable("F177").isPresent();
    }

    @Override
    public RuleEvaluation evaluate(QualityEvaluationContext context) {

        if (context.referenceYears().isEmpty()) {
            return RuleEvaluation.notEvaluable(
                code(),
                type(),
                provenance(),
                severity(),
                "Foreign current liabilities cannot be evaluated.",
                "No reference year is available for F176/F177.",
                null,
                List.of("F176", "F177"),
                Map.of());
        }

        int year = context.latestReferenceYear();

        var currentLiabilities = context.numericValue("F176", year);
        var foreignCurrentLiabilities = context.numericValue("F177", year);

        if (currentLiabilities.isEmpty() || foreignCurrentLiabilities.isEmpty()) {
            return RuleEvaluation.notEvaluable(
                code(),
                type(),
                provenance(),
                severity(),
                "Foreign current liabilities cannot be evaluated.",
                "F176 or F177 is missing for the evaluated reference year.",
                year,
                List.of("F176", "F177"),
                Map.of());
        }

        BigDecimal f176 = currentLiabilities.get();
        BigDecimal f177 = foreignCurrentLiabilities.get();

        if (f177.compareTo(f176) <= 0) {
            return RuleEvaluation.passed(
                code(),
                type(),
                provenance(),
                severity(),
                "F177 does not exceed F176.",
                year,
                List.of("F176", "F177"),
                Map.of(
                    "currentLiabilitiesF176", f176,
                    "foreignCurrentLiabilitiesF177", f177));
        }

        return RuleEvaluation.failed(
            code(),
            type(),
            provenance(),
            severity(),
            "F177 exceeds F176.",
            "Foreign current liabilities exceed total current liabilities.",
            "F177 should not exceed F176.",
            "Review the reported current liabilities and their foreign component.",
            year,
            List.of("F176", "F177"),
            Map.of(
                "currentLiabilitiesF176", f176,
                "foreignCurrentLiabilitiesF177", f177,
                "excess", f177.subtract(f176)));
    }
}
