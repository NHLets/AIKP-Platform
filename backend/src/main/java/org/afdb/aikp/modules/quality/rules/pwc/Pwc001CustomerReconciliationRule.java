package org.afdb.aikp.modules.quality.rules.pwc;

import org.afdb.aikp.modules.quality.domain.enums.QualityRuleProvenance;
import org.afdb.aikp.modules.quality.domain.enums.QualityRuleType;
import org.afdb.aikp.modules.quality.domain.enums.QualitySeverity;
import org.afdb.aikp.modules.quality.domain.service.QualityEvaluationContext;
import org.afdb.aikp.modules.quality.domain.service.QualityRule;
import org.afdb.aikp.modules.quality.domain.model.RuleEvaluation;
import org.afdb.aikp.modules.quality.domain.service.QualityRuleSupport;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
public final class Pwc001CustomerReconciliationRule implements QualityRule {

    @Override
    public String code() {
        return "PWC-001";
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
        return QualitySeverity.MAJOR;
    }

    @Override
    public boolean appliesTo(QualityEvaluationContext context) {
        return context.variable("A214").isPresent()
                && context.variable("A261").isPresent()
                && context.variable("A262").isPresent();
    }

    @Override
    public RuleEvaluation evaluate(QualityEvaluationContext context) {
        if (context.referenceYears().isEmpty()) {
            return RuleEvaluation.notEvaluable(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "Customer reconciliation cannot be evaluated.",
                    "No reference year is available for A214, A261 and A262.",
                    null,
                    List.of("A214", "A261", "A262"),
                    Map.of()
            );
        }

        int year = context.latestReferenceYear();

        var a214 = context.numericValue("A214", year);
        var a261 = context.numericValue("A261", year);
        var a262 = context.numericValue("A262", year);

        if (a214.isEmpty() || a261.isEmpty() || a262.isEmpty()) {
            return RuleEvaluation.notEvaluable(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "Customer reconciliation cannot be evaluated.",
                    "A214, A261 and A262 must all be available for the evaluated reference year.",
                    year,
                    List.of("A214", "A261", "A262"),
                    Map.of()
            );
        }

        BigDecimal observed = a214.get();
        BigDecimal calculated = a261.get().add(a262.get());

        Map<String, Object> evidence = Map.of(
                "reportedA214", observed,
                "calculatedA214", calculated,
                "a261", a261.get(),
                "a262", a262.get(),
                "relativeDifference",
                QualityRuleSupport.relativeDifference(observed, calculated)
        );

        if (QualityRuleSupport.approximatelyEqual(observed, calculated)) {
            return RuleEvaluation.passed(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "A214 is consistent with A261 + A262.",
                    year,
                    List.of("A214", "A261", "A262"),
                    evidence
            );
        }

        return RuleEvaluation.failed(
                code(),
                type(),
                provenance(),
                severity(),
                "A214 is inconsistent with A261 + A262.",
                "The reported total customers differs materially from the sum of the two reported customer categories.",
                "The reported total does not reconcile with the sum of A261 and A262.",
                "Review A214, A261 and A262 and verify the underlying customer data.",
                year,
                List.of("A214", "A261", "A262"),
                evidence
        );
    }
}
