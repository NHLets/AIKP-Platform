package org.afdb.aikp.modules.quality.domain.model;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.afdb.aikp.modules.quality.domain.enums.QualityFindingStatus;
import org.afdb.aikp.modules.quality.domain.enums.QualityRuleProvenance;
import org.afdb.aikp.modules.quality.domain.enums.QualityRuleType;
import org.afdb.aikp.modules.quality.domain.enums.QualitySeverity;
import org.afdb.aikp.modules.quality.domain.valueobject.QualityFindingId;
import org.afdb.aikp.modules.quality.domain.valueobject.QualityRunId;

public final class QualityFinding {

    private final QualityFindingId id;
    private final QualityRunId qualityRunId;
    private final String ruleCode;
    private final QualityRuleType ruleType;
    private final QualityRuleProvenance provenance;
    private final QualitySeverity severity;
    private final QualityFindingStatus status;
    private final String title;
    private final String message;
    private final String explanation;
    private final String recommendation;
    private final Integer referenceYear;
    private final List<String> affectedVariableCodes;
    private final Map<String, Object> evidence;

    private QualityFinding(
            QualityFindingId id,
            QualityRunId qualityRunId,
            RuleEvaluation evaluation) {
        this.id = Objects.requireNonNull(id);
        this.qualityRunId = Objects.requireNonNull(qualityRunId);
        this.ruleCode = evaluation.ruleCode();
        this.ruleType = evaluation.ruleType();
        this.provenance = evaluation.provenance();
        this.severity = evaluation.severity();
        this.status = evaluation.status();
        this.title = evaluation.title();
        this.message = evaluation.message();
        this.explanation = evaluation.explanation();
        this.recommendation = evaluation.recommendation();
        this.referenceYear = evaluation.referenceYear();
        this.affectedVariableCodes = evaluation.affectedVariableCodes();
        this.evidence = evaluation.evidence();
    }

    public static QualityFinding from(
            QualityRunId runId,
            RuleEvaluation evaluation) {
        if (evaluation.status() != QualityFindingStatus.FAILED
                && evaluation.status() != QualityFindingStatus.ERROR) {
            throw new IllegalArgumentException(
                    "Only FAILED and ERROR evaluations create findings.");
        }

        return new QualityFinding(
                QualityFindingId.generate(), runId, evaluation);
    }

    public static QualityFinding reconstitute(
            QualityFindingId id,
            QualityRunId qualityRunId,
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

        RuleEvaluation evaluation = new RuleEvaluation(
                ruleCode,
                ruleType,
                provenance,
                severity,
                status,
                title,
                message,
                explanation,
                recommendation,
                referenceYear,
                affectedVariableCodes,
                evidence);

        return new QualityFinding(id, qualityRunId, evaluation);
    }

    public QualityFindingId getId() { return id; }
    public QualityRunId getQualityRunId() { return qualityRunId; }
    public String getRuleCode() { return ruleCode; }
    public QualityRuleType getRuleType() { return ruleType; }
    public QualityRuleProvenance getProvenance() { return provenance; }
    public QualitySeverity getSeverity() { return severity; }
    public QualityFindingStatus getStatus() { return status; }
    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public String getExplanation() { return explanation; }
    public String getRecommendation() { return recommendation; }
    public Integer getReferenceYear() { return referenceYear; }
    public List<String> getAffectedVariableCodes() { return affectedVariableCodes; }
    public Map<String, Object> getEvidence() { return evidence; }
}
