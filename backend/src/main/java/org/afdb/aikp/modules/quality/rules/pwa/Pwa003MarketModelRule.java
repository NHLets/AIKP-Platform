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
public final class Pwa003MarketModelRule implements QualityRule {

    @Override
    public String code() {
        return "PWA-003";
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
        return context.variable("D012").isPresent();
    }

    @Override
    public RuleEvaluation evaluate(QualityEvaluationContext context) {

        if (context.referenceYears().isEmpty()) {
            return notEvaluable("No reference year is available for D012.", null);
        }

        int year = context.latestReferenceYear();
        var value = context.numericValue("D012", year);

        if (value.isEmpty()) {
            return notEvaluable(
                    "D012 is missing or not provided for the evaluated reference year.",
                    year);
        }

        BigDecimal marketModel = value.get();

        Map<String, Object> evidence = Map.of(
                "reportedD012", marketModel,
                "allowedValues", List.of(0, 1, 2, 3)
        );

        if (isInteger(marketModel)
                && marketModel.compareTo(BigDecimal.ZERO) >= 0
                && marketModel.compareTo(BigDecimal.valueOf(3)) <= 0) {

            return RuleEvaluation.passed(
                    code(), type(), provenance(), severity(),
                    "D012 has a valid code.",
                    year,
                    List.of("D012"),
                    evidence
            );
        }

        return RuleEvaluation.failed(
                code(), type(), provenance(), severity(),
                "D012 has an invalid code.",
                "The reported D012 value is outside the coding domain defined by the questionnaire.",
                "D012 must use 0=same company, 1=single buyer, 2=wholesale competition, or 3=retail competition.",
                "Review the D012 value and correct the coding if necessary.",
                year,
                List.of("D012"),
                evidence
        );
    }

    private RuleEvaluation notEvaluable(String explanation, Integer year) {
        return RuleEvaluation.notEvaluable(
                code(), type(), provenance(), severity(),
                "D012 cannot be evaluated.",
                explanation,
                year,
                List.of("D012"),
                Map.of()
        );
    }

    private static boolean isInteger(BigDecimal value) {
        return value.stripTrailingZeros().scale() <= 0;
    }
}
