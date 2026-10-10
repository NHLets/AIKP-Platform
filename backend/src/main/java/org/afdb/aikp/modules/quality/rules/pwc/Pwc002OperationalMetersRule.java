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
public final class Pwc002OperationalMetersRule implements QualityRule {

    @Override
    public String code() {
        return "PWC-002";
    }

    @Override
    public QualityRuleType type() {
        return QualityRuleType.LOGICAL;
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
        return context.variable("B048").isPresent()
                && context.variable("B049").isPresent();
    }

    @Override
    public RuleEvaluation evaluate(QualityEvaluationContext context) {
        if (context.referenceYears().isEmpty()) {
            return RuleEvaluation.notEvaluable(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "Operational meters cannot be evaluated.",
                    "No reference year is available for B048 and B049.",
                    null,
                    List.of("B048", "B049"),
                    Map.of()
            );
        }

        int year = context.latestReferenceYear();

        var b048 = context.numericValue("B048", year);
        var b049 = context.numericValue("B049", year);

        if (b048.isEmpty() || b049.isEmpty()) {
            return RuleEvaluation.notEvaluable(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "Operational meters cannot be evaluated.",
                    "B048 and B049 must both be available for the evaluated reference year.",
                    year,
                    List.of("B048", "B049"),
                    Map.of()
            );
        }

        BigDecimal installed = b048.get();
        BigDecimal operational = b049.get();

        Map<String, Object> evidence = Map.of(
                "installedMetersB048", installed,
                "operationalMetersB049", operational
        );

        if (operational.compareTo(installed) <= 0) {
            return RuleEvaluation.passed(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "B049 does not exceed B048.",
                    year,
                    List.of("B048", "B049"),
                    evidence
            );
        }

        return RuleEvaluation.failed(
                code(),
                type(),
                provenance(),
                severity(),
                "B049 exceeds B048.",
                "The reported number of operational meters is greater than the reported number of installed meters.",
                "Operational meters should not exceed installed meters.",
                "Review B048 and B049 and verify the underlying metering data.",
                year,
                List.of("B048", "B049"),
                evidence
        );
    }
}
