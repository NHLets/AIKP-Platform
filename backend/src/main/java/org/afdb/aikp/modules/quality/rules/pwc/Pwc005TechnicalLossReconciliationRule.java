package org.afdb.aikp.modules.quality.rules.pwc;

import org.afdb.aikp.modules.quality.domain.enums.QualityRuleProvenance;
import org.afdb.aikp.modules.quality.domain.enums.QualityRuleType;
import org.afdb.aikp.modules.quality.domain.enums.QualitySeverity;
import org.afdb.aikp.modules.quality.domain.model.RuleEvaluation;
import org.afdb.aikp.modules.quality.domain.service.QualityEvaluationContext;
import org.afdb.aikp.modules.quality.domain.service.QualityRule;
import org.afdb.aikp.modules.quality.domain.service.QualityRuleSupport;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
public final class Pwc005TechnicalLossReconciliationRule implements QualityRule {

    @Override
    public String code() {
        return "PWC-005";
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
        return context.variable("B053").isPresent()
                && context.variable("B055").isPresent()
                && context.variable("B056").isPresent();
    }

    @Override
    public RuleEvaluation evaluate(QualityEvaluationContext context) {
        if (context.referenceYears().isEmpty()) {
            return RuleEvaluation.notEvaluable(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "Technical loss reconciliation cannot be evaluated.",
                    "No reference year is available for B053, B055 and B056.",
                    null,
                    List.of("B053", "B055", "B056"),
                    Map.of()
            );
        }

        int year = context.latestReferenceYear();

        var b053 = context.numericValue("B053", year);
        var b055 = context.numericValue("B055", year);
        var b056 = context.numericValue("B056", year);

        if (b053.isEmpty() || b055.isEmpty() || b056.isEmpty()) {
            return RuleEvaluation.notEvaluable(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "Technical loss reconciliation cannot be evaluated.",
                    "B053, B055 and B056 must all be available for the evaluated reference year.",
                    year,
                    List.of("B053", "B055", "B056"),
                    Map.of()
            );
        }

        BigDecimal observed = b053.get();
        BigDecimal calculated = b055.get().add(b056.get());

        Map<String, Object> evidence = Map.of(
                "reportedB053", observed,
                "transmissionTechnicalLossesB055", b055.get(),
                "distributionTechnicalLossesB056", b056.get(),
                "calculatedB053", calculated,
                "relativeDifference",
                QualityRuleSupport.relativeDifference(observed, calculated)
        );

        if (QualityRuleSupport.approximatelyEqual(observed, calculated)) {
            return RuleEvaluation.passed(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "B053 is consistent with B055 + B056.",
                    year,
                    List.of("B053", "B055", "B056"),
                    evidence
            );
        }

        return RuleEvaluation.failed(
                code(),
                type(),
                provenance(),
                severity(),
                "B053 is inconsistent with B055 + B056.",
                "The reported technical losses differ materially from the sum of transmission and distribution technical losses.",
                "Technical losses should reconcile with transmission technical losses plus distribution technical losses.",
                "Review B053, B055 and B056 and verify the underlying loss calculations.",
                year,
                List.of("B053", "B055", "B056"),
                evidence
        );
    }
}
