package org.afdb.aikp.modules.quality.rules.pfg;

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
public final class Pfg005TotalAssetsReconciliationRule implements QualityRule {

    @Override
    public String code() {
        return "FG-005";
    }

    @Override
    public QualityRuleType type() {
        return QualityRuleType.RECONCILIATION;
    }

    @Override
    public QualityRuleProvenance provenance() {
        return QualityRuleProvenance.SOURCE_DEFINED;
    }

    @Override
    public QualitySeverity severity() {
        return QualitySeverity.CRITICAL;
    }

    @Override
    public boolean appliesTo(QualityEvaluationContext context) {
        return context.variable("F169").isPresent()
                && context.variable("F170").isPresent()
                && context.variable("F175").isPresent();
    }

    @Override
    public RuleEvaluation evaluate(QualityEvaluationContext context) {
        if (context.referenceYears().isEmpty()) {
            return RuleEvaluation.notEvaluable(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "Total assets reconciliation cannot be evaluated.",
                    "No reference year is available for F169, F170 and F175.",
                    null,
                    List.of("F169", "F170", "F175"),
                    Map.of()
            );
        }

        int year = context.latestReferenceYear();

        var f169 = context.numericValue("F169", year);
        var f170 = context.numericValue("F170", year);
        var f175 = context.numericValue("F175", year);

        if (f169.isEmpty() || f170.isEmpty() || f175.isEmpty()) {
            return RuleEvaluation.notEvaluable(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "Total assets reconciliation cannot be evaluated.",
                    "F169, F170 and F175 must all be available for the evaluated reference year.",
                    year,
                    List.of("F169", "F170", "F175"),
                    Map.of()
            );
        }

        BigDecimal currentAssets = f169.get();
        BigDecimal noncurrentAssets = f170.get();
        BigDecimal reportedTotalAssets = f175.get();

        BigDecimal calculatedTotalAssets = currentAssets.add(noncurrentAssets);

        Map<String, Object> evidence = Map.of(
                "currentAssetsF169", currentAssets,
                "noncurrentAssetsF170", noncurrentAssets,
                "reportedTotalAssetsF175", reportedTotalAssets,
                "calculatedTotalAssets", calculatedTotalAssets,
                "relativeDifference",
                QualityRuleSupport.relativeDifference(
                        reportedTotalAssets,
                        calculatedTotalAssets)
        );

        if (QualityRuleSupport.approximatelyEqual(
                reportedTotalAssets,
                calculatedTotalAssets)) {
            return RuleEvaluation.passed(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "F175 is consistent with F169 + F170.",
                    year,
                    List.of("F169", "F170", "F175"),
                    evidence
            );
        }

        return RuleEvaluation.failed(
                code(),
                type(),
                provenance(),
                severity(),
                "F175 is inconsistent with F169 + F170.",
                "Reported total assets differ materially from the sum of current and noncurrent assets.",
                "F175 should reconcile with F169 + F170 according to the F_G source definition.",
                "Review F169, F170 and F175 and verify the reported asset balances.",
                year,
                List.of("F169", "F170", "F175"),
                evidence
        );
    }
}
