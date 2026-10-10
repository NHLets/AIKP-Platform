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
public final class Pfg004GovernmentDividendsRule implements QualityRule {

    @Override
    public String code() {
        return "FG-004";
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
        return context.variable("F165").isPresent()
                && context.variable("F166").isPresent();
    }

    @Override
    public RuleEvaluation evaluate(QualityEvaluationContext context) {
        if (context.referenceYears().isEmpty()) {
            return RuleEvaluation.notEvaluable(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "Government dividends cannot be evaluated.",
                    "No reference year is available for F165 and F166.",
                    null,
                    List.of("F165", "F166"),
                    Map.of()
            );
        }

        int year = context.latestReferenceYear();

        var f165 = context.numericValue("F165", year);
        var f166 = context.numericValue("F166", year);

        if (f165.isEmpty() || f166.isEmpty()) {
            return RuleEvaluation.notEvaluable(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "Government dividends cannot be evaluated.",
                    "F165 and F166 must both be available for the evaluated reference year.",
                    year,
                    List.of("F165", "F166"),
                    Map.of()
            );
        }

        BigDecimal dividendsPaid = f165.get();
        BigDecimal governmentDividends = f166.get();

        Map<String, Object> evidence = Map.of(
                "dividendsPaidF165", dividendsPaid,
                "governmentDividendsF166", governmentDividends
        );

        if (governmentDividends.compareTo(dividendsPaid) <= 0) {
            return RuleEvaluation.passed(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "F166 is consistent with F165.",
                    year,
                    List.of("F165", "F166"),
                    evidence
            );
        }

        return RuleEvaluation.failed(
                code(),
                type(),
                provenance(),
                severity(),
                "F166 exceeds F165.",
                "Dividends paid to government cannot exceed total dividends paid.",
                "F166 should be less than or equal to F165 because F166 represents the dividends paid to government.",
                "Review F165 and F166 and verify the reported dividend breakdown.",
                year,
                List.of("F165", "F166"),
                evidence
        );
    }
}
