package org.afdb.aikp.modules.quality.rules.pwb;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.afdb.aikp.modules.quality.domain.enums.QualityFindingStatus;
import org.afdb.aikp.modules.quality.domain.enums.QualityRuleProvenance;
import org.afdb.aikp.modules.quality.domain.enums.QualityRuleType;
import org.afdb.aikp.modules.quality.domain.enums.QualitySeverity;
import org.afdb.aikp.modules.quality.domain.model.RuleEvaluation;
import org.afdb.aikp.modules.quality.domain.service.QualityEvaluationContext;
import org.afdb.aikp.modules.quality.domain.service.QualityRule;
import org.springframework.stereotype.Component;

@Component
public final class Pwb007MvRehabilitationLengthRule implements QualityRule {

    @Override
    public String code() {
        return "PWB-007";
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
        return context.variable("B026").isPresent()
                && context.variable("B027").isPresent();
    }

    @Override
    public RuleEvaluation evaluate(QualityEvaluationContext context) {
        if (context.referenceYears().isEmpty()) {
            return RuleEvaluation.notEvaluable(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "MV network rehabilitation cannot be evaluated.",
                    "No reference year is available for B026 and B027.",
                    null,
                    List.of("B026", "B027"),
                    Map.of());
        }

        int year = context.latestReferenceYear();

        var b026 = context.numericValue("B026", year);
        var b027 = context.numericValue("B027", year);

        if (b026.isEmpty() || b027.isEmpty()) {
            return RuleEvaluation.notEvaluable(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "MV network rehabilitation cannot be evaluated.",
                    "B026 and B027 must both be provided for the evaluated reference year.",
                    year,
                    List.of("B026", "B027"),
                    Map.of());
        }

        BigDecimal totalMvLength = b026.get();
        BigDecimal rehabilitationMvLength = b027.get();

        Map<String, Object> evidence = Map.of(
                "reportedB026", totalMvLength,
                "reportedB027", rehabilitationMvLength);

        if (rehabilitationMvLength.compareTo(totalMvLength) <= 0) {
            return RuleEvaluation.passed(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "MV rehabilitation length is consistent with total MV network length.",
                    year,
                    List.of("B026", "B027"),
                    evidence);
        }

        return RuleEvaluation.failed(
                code(),
                type(),
                provenance(),
                severity(),
                "MV rehabilitation length exceeds total MV network length.",
                "B027 cannot exceed B026.",
                "The reported MV rehabilitation length is greater than the reported total MV network length.",
                "Review B026 and B027 and verify that both values refer to the same network scope and reference year.",
                year,
                List.of("B026", "B027"),
                evidence);
    }
}
