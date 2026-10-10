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
public final class Pfg006LiabilitiesEquityReconciliationRule implements QualityRule {

    @Override
    public String code() {
        return "FG-006";
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
        return context.variable("F176").isPresent()
                && context.variable("F178").isPresent()
                && context.variable("F180").isPresent()
                && context.variable("F182").isPresent();
    }

    @Override
    public RuleEvaluation evaluate(QualityEvaluationContext context) {
        if (context.referenceYears().isEmpty()) {
            return RuleEvaluation.notEvaluable(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "Liabilities and equity reconciliation cannot be evaluated.",
                    "No reference year is available for F176, F178, F180 and F182.",
                    null,
                    List.of("F176", "F178", "F180", "F182"),
                    Map.of()
            );
        }

        int year = context.latestReferenceYear();

        var f176 = context.numericValue("F176", year);
        var f178 = context.numericValue("F178", year);
        var f180 = context.numericValue("F180", year);
        var f182 = context.numericValue("F182", year);

        if (f176.isEmpty() || f178.isEmpty() || f180.isEmpty() || f182.isEmpty()) {
            return RuleEvaluation.notEvaluable(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "Liabilities and equity reconciliation cannot be evaluated.",
                    "F176, F178, F180 and F182 must all be available for the evaluated reference year.",
                    year,
                    List.of("F176", "F178", "F180", "F182"),
                    Map.of()
            );
        }

        BigDecimal currentLiabilities = f176.get();
        BigDecimal longTermLiabilities = f178.get();
        BigDecimal equityAndReserves = f180.get();
        BigDecimal reportedTotal = f182.get();

        BigDecimal calculatedTotal = currentLiabilities
                .add(longTermLiabilities)
                .add(equityAndReserves);

        Map<String, Object> evidence = Map.of(
                "currentLiabilitiesF176", currentLiabilities,
                "longTermLiabilitiesF178", longTermLiabilities,
                "equityAndReservesF180", equityAndReserves,
                "reportedTotalF182", reportedTotal,
                "calculatedTotalF182", calculatedTotal,
                "relativeDifference",
                QualityRuleSupport.relativeDifference(
                        reportedTotal,
                        calculatedTotal)
        );

        if (QualityRuleSupport.approximatelyEqual(
                reportedTotal,
                calculatedTotal)) {
            return RuleEvaluation.passed(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "F182 is consistent with F176 + F178 + F180.",
                    year,
                    List.of("F176", "F178", "F180", "F182"),
                    evidence
            );
        }

        return RuleEvaluation.failed(
                code(),
                type(),
                provenance(),
                severity(),
                "F182 is inconsistent with F176 + F178 + F180.",
                "Reported total liabilities and equity differ materially from the sum of current liabilities, long-term liabilities and equity and reserves.",
                "F182 should reconcile with F176 + F178 + F180 according to the F_G source definition.",
                "Review F176, F178, F180 and F182 and verify the reported balance-sheet totals.",
                year,
                List.of("F176", "F178", "F180", "F182"),
                evidence
        );
    }
}
