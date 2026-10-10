package org.afdb.aikp.modules.quality.rules.pwb;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import org.afdb.aikp.modules.quality.domain.enums.*;
import org.afdb.aikp.modules.quality.domain.model.RuleEvaluation;
import org.afdb.aikp.modules.quality.domain.service.QualityEvaluationContext;
import org.afdb.aikp.modules.quality.domain.service.QualityRule;

@Component
public final class Pwb005TransmissionRehabilitationLengthRule
        implements QualityRule {

    public String code() { return "PWB-005"; }
    public QualityRuleType type() { return QualityRuleType.LOGICAL; }
    public QualityRuleProvenance provenance() { return QualityRuleProvenance.PROPOSED; }
    public QualitySeverity severity() { return QualitySeverity.MAJOR; }

    public boolean appliesTo(QualityEvaluationContext context) {
        return context.variable("B025").isPresent();
    }

    public RuleEvaluation evaluate(QualityEvaluationContext context) {
        int year = context.latestReferenceYear();

        var b024 = context.numericValue("B024", year);
        var b025 = context.numericValue("B025", year);

        if (b024.isEmpty() || b025.isEmpty()) {
            return RuleEvaluation.notEvaluable(
                    code(), type(), provenance(), severity(),
                    "Transmission rehabilitation length cannot be evaluated.",
                    "B024 and B025 must both be provided for the evaluated reference year.",
                    year,
                    List.of("B024", "B025"),
                    Map.of());
        }

        BigDecimal totalLength = b024.get();
        BigDecimal rehabilitationLength = b025.get();

        Map<String, Object> evidence = Map.of(
                "transmissionLengthKm", totalLength,
                "rehabilitationLengthKm", rehabilitationLength,
                "differenceKm", rehabilitationLength.subtract(totalLength));

        if (rehabilitationLength.compareTo(totalLength) <= 0) {
            return RuleEvaluation.passed(
                    code(), type(), provenance(), severity(),
                    "Transmission rehabilitation length is consistent with total transmission length.",
                    year,
                    List.of("B024", "B025"),
                    evidence);
        }

        return RuleEvaluation.failed(
                code(), type(), provenance(), severity(),
                "Transmission rehabilitation length exceeds total transmission length.",
                "B025 is greater than B024.",
                "The reported length of the high voltage transmission network in need of rehabilitation exceeds the reported total high voltage transmission network length.",
                "Review the reported values for B024 and B025.",
                year,
                List.of("B024", "B025"),
                evidence);
    }
}
