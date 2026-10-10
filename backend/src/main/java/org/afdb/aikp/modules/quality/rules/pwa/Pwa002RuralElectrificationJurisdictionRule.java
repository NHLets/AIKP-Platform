package org.afdb.aikp.modules.quality.rules.pwa;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.afdb.aikp.modules.quality.domain.enums.QualityRuleProvenance;
import org.afdb.aikp.modules.quality.domain.enums.QualityRuleType;
import org.afdb.aikp.modules.quality.domain.enums.QualitySeverity;
import org.afdb.aikp.modules.quality.domain.model.RuleEvaluation;
import org.afdb.aikp.modules.quality.domain.service.QualityEvaluationContext;
import org.afdb.aikp.modules.quality.domain.service.QualityRule;
import org.springframework.stereotype.Component;

@Component
public final class Pwa002RuralElectrificationJurisdictionRule implements QualityRule {

    @Override
    public String code() {
        return "PWA-002";
    }

    @Override
    public QualityRuleType type() {
        return QualityRuleType.STRUCTURAL;
    }

    @Override
    public QualityRuleProvenance provenance() {
        return QualityRuleProvenance.SOURCE_DEFINED;
    }

    @Override
    public QualitySeverity severity() {
        return QualitySeverity.MAJOR;
    }

    @Override
    public boolean appliesTo(QualityEvaluationContext context) {
        return context.variable("D007").isPresent();
    }

    @Override
    public RuleEvaluation evaluate(QualityEvaluationContext context) {

        if (context.referenceYears().isEmpty()) {
            return notEvaluable("No reference year is available for D007.");
        }

        int year = context.latestReferenceYear();
        var value = context.numericValue("D007", year);

        if (value.isEmpty()) {
            return notEvaluable(
                    "D007 is missing or not provided for the evaluated reference year.");
        }

        BigDecimal jurisdiction = value.get();

        Map<String, Object> evidence = Map.of(
                "reportedD007", jurisdiction,
                "allowedValues", List.of(0, 1, 2)
        );

        if (isInteger(jurisdiction)
                && (jurisdiction.compareTo(BigDecimal.ZERO) == 0
                || jurisdiction.compareTo(BigDecimal.ONE) == 0
                || jurisdiction.compareTo(BigDecimal.valueOf(2)) == 0)) {

            return RuleEvaluation.passed(
                    code(), type(), provenance(), severity(),
                    "D007 has a valid code.",
                    year,
                    List.of("D007"),
                    evidence
            );
        }

        return RuleEvaluation.failed(
                code(), type(), provenance(), severity(),
                "D007 has an invalid code.",
                "The reported D007 value is outside the coding domain defined by the questionnaire.",
                "D007 must use 0=Central, 1=Regional, or 2=Local/Municipal.",
                "Review the D007 value and correct the coding if necessary.",
                year,
                List.of("D007"),
                evidence
        );
    }

    private RuleEvaluation notEvaluable(String explanation) {
        return RuleEvaluation.notEvaluable(
                code(), type(), provenance(), severity(),
                "D007 cannot be evaluated.",
                explanation,
                null,
                List.of("D007"),
                Map.of()
        );
    }

    private RuleEvaluation notEvaluable(
            String explanation,
            int year) {
        return RuleEvaluation.notEvaluable(
                code(), type(), provenance(), severity(),
                "D007 cannot be evaluated.",
                explanation,
                year,
                List.of("D007"),
                Map.of()
        );
    }

    private static boolean isInteger(BigDecimal value) {
        return value.stripTrailingZeros().scale() <= 0;
    }
}
