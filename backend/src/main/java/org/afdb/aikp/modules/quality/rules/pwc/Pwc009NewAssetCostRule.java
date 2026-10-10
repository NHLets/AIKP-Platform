package org.afdb.aikp.modules.quality.rules.pwc;

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
public final class Pwc009NewAssetCostRule implements QualityRule {

    @Override
    public String code() {
        return "PWC-009";
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
        return context.variable("B081").isPresent()
                && context.variable("B083").isPresent();
    }

    @Override
    public RuleEvaluation evaluate(QualityEvaluationContext context) {
        if (context.referenceYears().isEmpty()) {
            return RuleEvaluation.notEvaluable(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "New asset cost cannot be evaluated.",
                    "No reference year is available for B081 and B083.",
                    null,
                    List.of("B081", "B083"),
                    Map.of()
            );
        }

        int year = context.latestReferenceYear();

        var b081 = context.numericValue("B081", year);
        var b083 = context.numericValue("B083", year);

        if (b081.isEmpty() || b083.isEmpty()) {
            return RuleEvaluation.notEvaluable(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "New asset cost cannot be evaluated.",
                    "B081 and B083 must both be available for the evaluated reference year.",
                    year,
                    List.of("B081", "B083"),
                    Map.of()
            );
        }

        BigDecimal totalCapitalCost = b081.get();
        BigDecimal newAssetCost = b083.get();

        Map<String, Object> evidence = Map.of(
                "totalCapitalCostB081", totalCapitalCost,
                "newAssetCostB083", newAssetCost
        );

        if (newAssetCost.compareTo(totalCapitalCost) <= 0) {
            return RuleEvaluation.passed(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "B083 does not exceed B081.",
                    year,
                    List.of("B081", "B083"),
                    evidence
            );
        }

        return RuleEvaluation.failed(
                code(),
                type(),
                provenance(),
                severity(),
                "B083 exceeds B081.",
                "The reported cost of new assets is greater than the reported total capital cost.",
                "New asset cost should not exceed total capital cost.",
                "Review B081 and B083 and verify the underlying capital expenditure data.",
                year,
                List.of("B081", "B083"),
                evidence
        );
    }
}
