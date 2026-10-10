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
public final class Pfg001GoodsServicesReconciliationRule implements QualityRule {

    @Override
    public String code() {
        return "FG-001";
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
        return context.variable("F132").isPresent()
                && context.variable("F133").isPresent()
                && context.variable("F134").isPresent()
                && context.variable("F135").isPresent();
    }

    @Override
    public RuleEvaluation evaluate(QualityEvaluationContext context) {
        if (context.referenceYears().isEmpty()) {
            return RuleEvaluation.notEvaluable(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "Goods and services purchase reconciliation cannot be evaluated.",
                    "No reference year is available for F132, F133, F134 and F135.",
                    null,
                    List.of("F132", "F133", "F134", "F135"),
                    Map.of()
            );
        }

        int year = context.latestReferenceYear();

        var f132 = context.numericValue("F132", year);
        var f133 = context.numericValue("F133", year);
        var f134 = context.numericValue("F134", year);
        var f135 = context.numericValue("F135", year);

        if (f132.isEmpty() || f133.isEmpty() || f134.isEmpty() || f135.isEmpty()) {
            return RuleEvaluation.notEvaluable(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "Goods and services purchase reconciliation cannot be evaluated.",
                    "F132, F133, F134 and F135 must all be available for the evaluated reference year.",
                    year,
                    List.of("F132", "F133", "F134", "F135"),
                    Map.of()
            );
        }

        BigDecimal observed = f132.get();
        BigDecimal calculated = f133.get()
                .add(f134.get())
                .add(f135.get());

        Map<String, Object> evidence = Map.of(
                "reportedF132", observed,
                "fuelF133", f133.get(),
                "ppaFeesF134", f134.get(),
                "otherGoodsAndServicesF135", f135.get(),
                "calculatedF132", calculated,
                "relativeDifference",
                QualityRuleSupport.relativeDifference(observed, calculated)
        );

        if (QualityRuleSupport.approximatelyEqual(observed, calculated)) {
            return RuleEvaluation.passed(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "F132 is consistent with F133 + F134 + F135.",
                    year,
                    List.of("F132", "F133", "F134", "F135"),
                    evidence
            );
        }

        return RuleEvaluation.failed(
                code(),
                type(),
                provenance(),
                severity(),
                "F132 is inconsistent with F133 + F134 + F135.",
                "The reported purchases of goods and services directly used in production differ materially from the sum of fuel, PPA fees and other purchases.",
                "F132 should reconcile with F133 + F134 + F135 according to the F_G source structure.",
                "Review F132, F133, F134 and F135 and verify the underlying expenditure breakdown.",
                year,
                List.of("F132", "F133", "F134", "F135"),
                evidence
        );
    }
}
