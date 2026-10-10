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
public final class Pwc004SystemLossReconciliationRule implements QualityRule {

    @Override
    public String code() {
        return "PWC-004";
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
        return context.variable("B052").isPresent()
                && context.variable("B053").isPresent()
                && context.variable("B054").isPresent();
    }

    @Override
    public RuleEvaluation evaluate(QualityEvaluationContext context) {
        if (context.referenceYears().isEmpty()) {
            return RuleEvaluation.notEvaluable(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "System loss reconciliation cannot be evaluated.",
                    "No reference year is available for B052, B053 and B054.",
                    null,
                    List.of("B052", "B053", "B054"),
                    Map.of()
            );
        }

        int year = context.latestReferenceYear();

        var b052 = context.numericValue("B052", year);
        var b053 = context.numericValue("B053", year);
        var b054 = context.numericValue("B054", year);

        if (b052.isEmpty() || b053.isEmpty() || b054.isEmpty()) {
            return RuleEvaluation.notEvaluable(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "System loss reconciliation cannot be evaluated.",
                    "B052, B053 and B054 must all be available for the evaluated reference year.",
                    year,
                    List.of("B052", "B053", "B054"),
                    Map.of()
            );
        }

        BigDecimal observed = b052.get();
        BigDecimal calculated = b053.get().add(b054.get());

        Map<String, Object> evidence = Map.of(
                "reportedB052", observed,
                "technicalLossesB053", b053.get(),
                "nonTechnicalLossesB054", b054.get(),
                "calculatedB052", calculated,
                "relativeDifference",
                QualityRuleSupport.relativeDifference(observed, calculated)
        );

        if (QualityRuleSupport.approximatelyEqual(observed, calculated)) {
            return RuleEvaluation.passed(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "B052 is consistent with B053 + B054.",
                    year,
                    List.of("B052", "B053", "B054"),
                    evidence
            );
        }

        return RuleEvaluation.failed(
                code(),
                type(),
                provenance(),
                severity(),
                "B052 is inconsistent with B053 + B054.",
                "The reported system losses differ materially from the sum of technical and non-technical losses.",
                "System losses should reconcile with technical losses plus non-technical losses.",
                "Review B052, B053 and B054 and verify the underlying loss calculations.",
                year,
                List.of("B052", "B053", "B054"),
                evidence
        );
    }
}
