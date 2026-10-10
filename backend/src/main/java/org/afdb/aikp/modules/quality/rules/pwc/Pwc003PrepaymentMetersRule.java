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
public final class Pwc003PrepaymentMetersRule implements QualityRule {

    @Override
    public String code() {
        return "PWC-003";
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
        return context.variable("B050").isPresent()
                && context.variable("B051").isPresent();
    }

    @Override
    public RuleEvaluation evaluate(QualityEvaluationContext context) {
        if (context.referenceYears().isEmpty()) {
            return RuleEvaluation.notEvaluable(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "Prepayment meters cannot be evaluated.",
                    "No reference year is available for B050 and B051.",
                    null,
                    List.of("B050", "B051"),
                    Map.of()
            );
        }

        int year = context.latestReferenceYear();

        var b050 = context.numericValue("B050", year);
        var b051 = context.numericValue("B051", year);

        if (b050.isEmpty() || b051.isEmpty()) {
            return RuleEvaluation.notEvaluable(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "Prepayment meters cannot be evaluated.",
                    "B050 and B051 must both be available for the evaluated reference year.",
                    year,
                    List.of("B050", "B051"),
                    Map.of()
            );
        }

        BigDecimal prepaymentMeters = b050.get();
        BigDecimal operationalPrepaymentMeters = b051.get();

        Map<String, Object> evidence = Map.of(
                "prepaymentMetersB050", prepaymentMeters,
                "operationalPrepaymentMetersB051", operationalPrepaymentMeters
        );

        if (operationalPrepaymentMeters.compareTo(prepaymentMeters) <= 0) {
            return RuleEvaluation.passed(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "B051 does not exceed B050.",
                    year,
                    List.of("B050", "B051"),
                    evidence
            );
        }

        return RuleEvaluation.failed(
                code(),
                type(),
                provenance(),
                severity(),
                "B051 exceeds B050.",
                "The reported number of operational prepayment meters is greater than the reported number of prepayment meters.",
                "Operational prepayment meters should not exceed the total number of prepayment meters.",
                "Review B050 and B051 and verify the underlying metering data.",
                year,
                List.of("B050", "B051"),
                evidence
        );
    }
}
