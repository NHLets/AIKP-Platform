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
public final class Pwc008RehabilitationCostRule implements QualityRule {

    @Override
    public String code() {
        return "PWC-008";
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
                && context.variable("B082").isPresent();
    }

    @Override
    public RuleEvaluation evaluate(QualityEvaluationContext context) {
        if (context.referenceYears().isEmpty()) {
            return RuleEvaluation.notEvaluable(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "Rehabilitation cost cannot be evaluated.",
                    "No reference year is available for B081 and B082.",
                    null,
                    List.of("B081", "B082"),
                    Map.of()
            );
        }

        int year = context.latestReferenceYear();

        var b081 = context.numericValue("B081", year);
        var b082 = context.numericValue("B082", year);

        if (b081.isEmpty() || b082.isEmpty()) {
            return RuleEvaluation.notEvaluable(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "Rehabilitation cost cannot be evaluated.",
                    "B081 and B082 must both be available for the evaluated reference year.",
                    year,
                    List.of("B081", "B082"),
                    Map.of()
            );
        }

        BigDecimal totalCapitalCost = b081.get();
        BigDecimal rehabilitationCost = b082.get();

        Map<String, Object> evidence = Map.of(
                "totalCapitalCostB081", totalCapitalCost,
                "rehabilitationCostB082", rehabilitationCost
        );

        if (rehabilitationCost.compareTo(totalCapitalCost) <= 0) {
            return RuleEvaluation.passed(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "B082 does not exceed B081.",
                    year,
                    List.of("B081", "B082"),
                    evidence
            );
        }

        return RuleEvaluation.failed(
                code(),
                type(),
                provenance(),
                severity(),
                "B082 exceeds B081.",
                "The reported rehabilitation cost is greater than the reported total capital cost.",
                "Rehabilitation cost should not exceed total capital cost.",
                "Review B081 and B082 and verify the underlying capital expenditure data.",
                year,
                List.of("B081", "B082"),
                evidence
        );
    }
}
