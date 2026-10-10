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
public final class Pwc007OperationalCostComponentsRule implements QualityRule {

    @Override
    public String code() {
        return "PWC-007";
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
        return context.variable("B076").isPresent()
                && context.variable("B077").isPresent()
                && context.variable("B078").isPresent()
                && context.variable("B079").isPresent();
    }

    @Override
    public RuleEvaluation evaluate(QualityEvaluationContext context) {
        if (context.referenceYears().isEmpty()) {
            return RuleEvaluation.notEvaluable(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "Operational cost components cannot be evaluated.",
                    "No reference year is available for B076-B079.",
                    null,
                    List.of("B076", "B077", "B078", "B079"),
                    Map.of()
            );
        }

        int year = context.latestReferenceYear();

        var b076 = context.numericValue("B076", year);
        var b077 = context.numericValue("B077", year);
        var b078 = context.numericValue("B078", year);
        var b079 = context.numericValue("B079", year);

        if (b076.isEmpty() || b077.isEmpty() || b078.isEmpty() || b079.isEmpty()) {
            return RuleEvaluation.notEvaluable(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "Operational cost components cannot be evaluated.",
                    "B076, B077, B078 and B079 must all be available for the evaluated reference year.",
                    year,
                    List.of("B076", "B077", "B078", "B079"),
                    Map.of()
            );
        }

        BigDecimal total = b076.get();
        BigDecimal components = b077.get()
                .add(b078.get())
                .add(b079.get());

        Map<String, Object> evidence = Map.of(
                "reportedOperationalCostB076", total,
                "laborCostB077", b077.get(),
                "fuelCostB078", b078.get(),
                "maintenanceCostB079", b079.get(),
                "sumOfComponents", components
        );

        if (components.compareTo(total) <= 0) {
            return RuleEvaluation.passed(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "B077 + B078 + B079 does not exceed B076.",
                    year,
                    List.of("B076", "B077", "B078", "B079"),
                    evidence
            );
        }

        return RuleEvaluation.failed(
                code(),
                type(),
                provenance(),
                severity(),
                "B077 + B078 + B079 exceeds B076.",
                "The sum of the reported operational cost components is greater than the reported operational cost total.",
                "The identified operational cost components should not exceed the reported total operational cost.",
                "Review B076, B077, B078 and B079 and verify the underlying cost data.",
                year,
                List.of("B076", "B077", "B078", "B079"),
                evidence
        );
    }
}
