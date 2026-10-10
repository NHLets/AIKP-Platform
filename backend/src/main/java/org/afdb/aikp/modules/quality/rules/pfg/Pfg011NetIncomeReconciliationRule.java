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
public class Pfg011NetIncomeReconciliationRule implements QualityRule {

    @Override
    public String code() {
        return "FG-011";
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
        return context.variable("F152").isPresent()
                && context.variable("F153").isPresent()
                && context.variable("F155").isPresent();
    }

    @Override
    public RuleEvaluation evaluate(QualityEvaluationContext context) {

        if (context.referenceYears().isEmpty()) {
            return RuleEvaluation.notEvaluable(
                code(),
                type(),
                provenance(),
                severity(),
                "Net income reconciliation cannot be evaluated.",
                "No reference year is available for F152, F153 and F155.",
                null,
                List.of("F152", "F153", "F155"),
                Map.of());
        }

        int year = context.latestReferenceYear();

        var profitBeforeTax = context.numericValue("F152", year);
        var corporateIncomeTax = context.numericValue("F153", year);
        var netIncome = context.numericValue("F155", year);

        if (profitBeforeTax.isEmpty()
                || corporateIncomeTax.isEmpty()
                || netIncome.isEmpty()) {
            return RuleEvaluation.notEvaluable(
                code(),
                type(),
                provenance(),
                severity(),
                "Net income reconciliation cannot be evaluated.",
                "F152, F153 or F155 is missing for the evaluated reference year.",
                year,
                List.of("F152", "F153", "F155"),
                Map.of());
        }

        BigDecimal f152 = profitBeforeTax.get();
        BigDecimal f153 = corporateIncomeTax.get();
        BigDecimal f155 = netIncome.get();

        BigDecimal calculatedNetIncome = f152.subtract(f153);

        if (QualityRuleSupport.approximatelyEqual(f155, calculatedNetIncome)) {
            return RuleEvaluation.passed(
                code(),
                type(),
                provenance(),
                severity(),
                "F155 is consistent with F152 - F153.",
                year,
                List.of("F152", "F153", "F155"),
                Map.of(
                    "profitBeforeTaxF152", f152,
                    "corporateIncomeTaxF153", f153,
                    "reportedNetIncomeF155", f155,
                    "calculatedNetIncome", calculatedNetIncome,
                    "relativeDifference",
                    QualityRuleSupport.relativeDifference(
                        f155, calculatedNetIncome)));
        }

        return RuleEvaluation.failed(
            code(),
            type(),
            provenance(),
            severity(),
            "F155 is inconsistent with F152 - F153.",
            "Reported net income differs materially from profit before tax less corporate income tax.",
            "Review F152, F153 and F155 and verify the reported income statement figures.",
            "Check whether the reported net income, profit before tax or corporate income tax contains an error or an accounting treatment not captured by this proposed reconciliation.",
            year,
            List.of("F152", "F153", "F155"),
            Map.of(
                "profitBeforeTaxF152", f152,
                "corporateIncomeTaxF153", f153,
                "reportedNetIncomeF155", f155,
                "calculatedNetIncome", calculatedNetIncome,
                "relativeDifference",
                QualityRuleSupport.relativeDifference(
                    f155, calculatedNetIncome)));
    }
}
