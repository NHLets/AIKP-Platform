package org.afdb.aikp.modules.quality.rules.pwb;

import org.afdb.aikp.modules.quality.domain.enums.QualityRuleProvenance;
import org.afdb.aikp.modules.quality.domain.enums.QualityRuleType;
import org.afdb.aikp.modules.quality.domain.enums.QualitySeverity;
import org.afdb.aikp.modules.quality.domain.model.RuleEvaluation;
import org.afdb.aikp.modules.quality.domain.service.QualityEvaluationContext;
import org.afdb.aikp.modules.quality.domain.service.QualityRule;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@Component
public final class Pwb006GenerationReconciliationRule implements QualityRule {

    private static final BigDecimal RELATIVE_TOLERANCE =
            new BigDecimal("0.005");

    private static final BigDecimal ABSOLUTE_TOLERANCE =
            new BigDecimal("0.01");

    @Override
    public String code() {
        return "PWB-006";
    }

    @Override
    public QualityRuleType type() {
        return QualityRuleType.RECONCILIATION;
    }

    @Override
    public QualityRuleProvenance provenance() {
        return QualityRuleProvenance.PROPOSED;
    }

    @Override
    public QualitySeverity severity() {
        return QualitySeverity.CRITICAL;
    }

    @Override
    public boolean appliesTo(QualityEvaluationContext context) {
        return context.variable("B013").isPresent();
    }

    @Override
    public RuleEvaluation evaluate(QualityEvaluationContext context) {

        if (context.referenceYears().isEmpty()) {
            return RuleEvaluation.notEvaluable(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "Generation reconciliation cannot be evaluated.",
                    "No reference year is available for B013-B017.",
                    null,
                    List.of("B013", "B014", "B015", "B016", "B017"),
                    Map.of());
        }

        int year = context.latestReferenceYear();

        var b013 = context.numericValue("B013", year);
        var b014 = context.numericValue("B014", year);
        var b015 = context.numericValue("B015", year);
        var b016 = context.numericValue("B016", year);
        var b017 = context.numericValue("B017", year);

        if (b013.isEmpty()
                || b014.isEmpty()
                || b015.isEmpty()
                || b016.isEmpty()
                || b017.isEmpty()) {

            return RuleEvaluation.notEvaluable(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "Generation reconciliation cannot be evaluated.",
                    "B013 or one of its generation components B014-B017 is missing for the evaluated reference year.",
                    year,
                    List.of("B013", "B014", "B015", "B016", "B017"),
                    Map.of());
        }

        BigDecimal observed = b013.get();

        BigDecimal expected = b014.get()
                .add(b015.get())
                .add(b016.get())
                .add(b017.get());

        BigDecimal absoluteDifference =
                observed.subtract(expected).abs();

        BigDecimal scale = observed.abs().max(expected.abs());

        BigDecimal relativeDifference;

        if (scale.compareTo(BigDecimal.ZERO) == 0) {
            relativeDifference = BigDecimal.ZERO;
        } else {
            relativeDifference = absoluteDifference
                    .divide(scale, 10, RoundingMode.HALF_UP);
        }

        BigDecimal relativeTolerance =
                scale.multiply(RELATIVE_TOLERANCE);

        BigDecimal allowedDifference =
                relativeTolerance.max(ABSOLUTE_TOLERANCE);

        Map<String, Object> evidence = Map.of(
                "reportedB013", observed,
                "calculatedGeneration", expected,
                "absoluteDifference", absoluteDifference,
                "relativeDifference", relativeDifference,
                "allowedDifference", allowedDifference
        );

        if (absoluteDifference.compareTo(allowedDifference) <= 0) {
            return RuleEvaluation.passed(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "B013 reconciles with the sum of B014-B017.",
                    year,
                    List.of("B013", "B014", "B015", "B016", "B017"),
                    evidence);
        }

        return RuleEvaluation.failed(
                code(),
                type(),
                provenance(),
                severity(),
                "B013 does not reconcile with the sum of B014-B017.",
                "The reported total generation differs from the sum of the reported generation components beyond the configured tolerance.",
                "The difference exceeds the configured reconciliation tolerance.",
                "Review B013 and the generation component values B014-B017.",
                year,
                List.of("B013", "B014", "B015", "B016", "B017"),
                evidence);
    }
}
