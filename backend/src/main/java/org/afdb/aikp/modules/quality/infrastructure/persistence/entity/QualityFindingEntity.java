package org.afdb.aikp.modules.quality.infrastructure.persistence.entity;

import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.databind.JsonNode;

@Entity
@Table(name = "quality_finding", schema = "quality")
public class QualityFindingEntity {

    @Id
    private UUID id;

    @Column(name = "quality_run_id", nullable = false)
    private UUID qualityRunId;

    @Column(name = "rule_code", nullable = false, length = 100)
    private String ruleCode;

    @Column(name = "rule_type", nullable = false, length = 50)
    private String ruleType;

    @Column(name = "provenance", nullable = false, length = 30)
    private String provenance;

    @Column(name = "severity", nullable = false, length = 20)
    private String severity;

    @Column(name = "status", nullable = false, length = 30)
    private String status;

    @Column(name = "title", nullable = false, length = 500)
    private String title;

    @Column(name = "message")
    private String message;

    @Column(name = "explanation")
    private String explanation;

    @Column(name = "recommendation")
    private String recommendation;

    @Column(name = "reference_year")
    private Integer referenceYear;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "affected_variables", columnDefinition = "jsonb")
    private JsonNode affectedVariables;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "evidence", columnDefinition = "jsonb")
    private JsonNode evidence;

    public QualityFindingEntity() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getQualityRunId() {
        return qualityRunId;
    }

    public void setQualityRunId(UUID qualityRunId) {
        this.qualityRunId = qualityRunId;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    public String getProvenance() {
        return provenance;
    }

    public void setProvenance(String provenance) {
        this.provenance = provenance;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public Integer getReferenceYear() {
        return referenceYear;
    }

    public void setReferenceYear(Integer referenceYear) {
        this.referenceYear = referenceYear;
    }

    public JsonNode getAffectedVariables() {
        return affectedVariables;
    }

    public void setAffectedVariables(JsonNode affectedVariables) {
        this.affectedVariables = affectedVariables;
    }

    public JsonNode getEvidence() {
        return evidence;
    }

    public void setEvidence(JsonNode evidence) {
        this.evidence = evidence;
    }
}
