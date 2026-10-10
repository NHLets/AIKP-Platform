package org.afdb.aikp.modules.quality.domain.model;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.afdb.aikp.modules.quality.domain.enums.QualityFindingStatus;
import org.afdb.aikp.modules.quality.domain.enums.QualityRuleProvenance;
import org.afdb.aikp.modules.quality.domain.enums.QualityRuleType;
import org.afdb.aikp.modules.quality.domain.enums.QualitySeverity;

public record RuleEvaluation(
        String ruleCode,
        QualityRuleType ruleType,
        QualityRuleProvenance provenance,
        QualitySeverity severity,
        QualityFindingStatus status,
        String title,
        String message,
        String explanation,
        String recommendation,
        Integer referenceYear,
        List<String> affectedVariableCodes,
        Map<String, Object> evidence) {

    public RuleEvaluation {
        affectedVariableCodes = affectedVariableCodes == null
                ? List.of()
                : List.copyOf(affectedVariableCodes);
        evidence = evidence == null
                ? Map.of()
                : Collections.unmodifiableMap(new LinkedHashMap<>(evidence));
    }

    public static RuleEvaluation passed(
            String ruleCode,
            QualityRuleType type,
            QualityRuleProvenance provenance,
            QualitySeverity severity,
            String title,
            Integer year,
            List<String> variables,
            Map<String, Object> evidence) {
        return new RuleEvaluation(
                ruleCode, type, provenance, severity,
                QualityFindingStatus.PASSED, title, null, null, null,
                year, variables, evidence);
    }

    public static RuleEvaluation failed(
            String ruleCode,
            QualityRuleType type,
            QualityRuleProvenance provenance,
            QualitySeverity severity,
            String title,
            String message,
            String explanation,
            String recommendation,
            Integer year,
            List<String> variables,
            Map<String, Object> evidence) {
        return new RuleEvaluation(
                ruleCode, type, provenance, severity,
                QualityFindingStatus.FAILED, title, message,
                explanation, recommendation, year, variables, evidence);
    }

    public static RuleEvaluation notEvaluable(
            String ruleCode,
            QualityRuleType type,
            QualityRuleProvenance provenance,
            QualitySeverity severity,
            String title,
            String message,
            Integer year,
            List<String> variables,
            Map<String, Object> evidence) {
        return new RuleEvaluation(
                ruleCode, type, provenance, severity,
                QualityFindingStatus.NOT_EVALUABLE, title, message,
                null, null, year, variables, evidence);
    }

    public static RuleEvaluation notApplicable(
            String ruleCode,
            QualityRuleType type,
            QualityRuleProvenance provenance,
            QualitySeverity severity,
            String title,
            String message) {
        return new RuleEvaluation(
                ruleCode, type, provenance, severity,
                QualityFindingStatus.NOT_APPLICABLE, title, message,
                null, null, null, List.of(), Map.of());
    }

    public static RuleEvaluation error(
            String ruleCode,
            QualityRuleType type,
            QualityRuleProvenance provenance,
            QualitySeverity severity,
            String title,
            String message,
            Integer year,
            List<String> variables) {
        return new RuleEvaluation(
                ruleCode, type, provenance, severity,
                QualityFindingStatus.ERROR, title, message,
                null, null, year, variables, Map.of());
    }
}
