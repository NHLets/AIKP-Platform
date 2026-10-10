package org.afdb.aikp.modules.quality.rules.pwb;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import org.afdb.aikp.modules.quality.domain.enums.*;
import org.afdb.aikp.modules.quality.domain.model.RuleEvaluation;
import org.afdb.aikp.modules.quality.domain.service.QualityEvaluationContext;
import org.afdb.aikp.modules.quality.domain.service.QualityRule;
import org.afdb.aikp.modules.quality.domain.service.QualityRuleSupport;

@Component
public final class Pwb002InstalledCapacityReconciliationRule
        implements QualityRule {

    public String code() { return "PWB-002"; }
    public QualityRuleType type() { return QualityRuleType.RECONCILIATION; }
    public QualityRuleProvenance provenance() { return QualityRuleProvenance.PROPOSED; }
    public QualitySeverity severity() { return QualitySeverity.CRITICAL; }

    public boolean appliesTo(QualityEvaluationContext context) {
        return context.variable("B001").isPresent();
    }

    public RuleEvaluation evaluate(QualityEvaluationContext context) {
        int year = context.latestReferenceYear();

        var b001 = context.numericValue("B001", year);
        var b002 = context.numericValue("B002", year);
        var b003 = context.numericValue("B003", year);
        var b004 = context.numericValue("B004", year);
        var b005 = context.numericValue("B005", year);

        if (b001.isEmpty() || b002.isEmpty() || b003.isEmpty()
                || b004.isEmpty() || b005.isEmpty()) {
            return RuleEvaluation.notEvaluable(
                    code(), type(), provenance(), severity(),
                    "Generation reconciliation cannot be evaluated.",
                    "One or more required generation observations are missing.",
                    year,
                    List.of("B001", "B002", "B003", "B004", "B005"),
                    Map.of());
        }

        BigDecimal expected = b002.get()
                .add(b003.get())
                .add(b004.get())
                .add(b005.get());

        BigDecimal observed = b001.get();

        Map<String, Object> evidence = Map.of(
                "reportedB001", observed,
                "calculatedGeneration", expected,
                "relativeDifference",
                QualityRuleSupport.relativeDifference(observed, expected));

        if (QualityRuleSupport.approximatelyEqual(observed, expected)) {
            return RuleEvaluation.passed(
                    code(), type(), provenance(), severity(),
                    "Total generation reconciles with capacity components.",
                    year,
                    List.of("B001", "B002", "B003", "B004", "B005"),
                    evidence);
        }

        return RuleEvaluation.failed(
                code(), type(), provenance(), severity(),
                "Generation reconciliation discrepancy detected.",
                "B001 does not reconcile with the sum of B002 to B005.",
                "The reported total generation differs from the sum of the reported capacity components beyond the configured tolerance.",
                "Review B001 and the generation component values B002-B005.",
                year,
                List.of("B001", "B002", "B003", "B004", "B005"),
                evidence);
    }
}
