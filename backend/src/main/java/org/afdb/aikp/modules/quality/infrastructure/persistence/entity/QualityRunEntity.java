package org.afdb.aikp.modules.quality.infrastructure.persistence.entity;

import java.time.OffsetDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "quality_run", schema = "quality")
public class QualityRunEntity {

    @Id
    private UUID id;

    @Column(name = "data_collection_id", nullable = false)
    private UUID dataCollectionId;

    @Column(name = "trigger", nullable = false, length = 30)
    private String trigger;

    @Column(name = "status", nullable = false, length = 30)
    private String status;

    @Column(name = "started_at", nullable = false)
    private OffsetDateTime startedAt;

    @Column(name = "completed_at")
    private OffsetDateTime completedAt;

    @Column(name = "rules_evaluated", nullable = false)
    private int rulesEvaluated;

    @Column(name = "rules_passed", nullable = false)
    private int rulesPassed;

    @Column(name = "rules_failed", nullable = false)
    private int rulesFailed;

    @Column(name = "rules_not_evaluable", nullable = false)
    private int rulesNotEvaluable;

    @Column(name = "rules_not_applicable", nullable = false)
    private int rulesNotApplicable;

    @Column(name = "rules_errored", nullable = false)
    private int rulesErrored;

    public QualityRunEntity() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getDataCollectionId() {
        return dataCollectionId;
    }

    public void setDataCollectionId(UUID dataCollectionId) {
        this.dataCollectionId = dataCollectionId;
    }

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public OffsetDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(OffsetDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public OffsetDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(OffsetDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public int getRulesEvaluated() {
        return rulesEvaluated;
    }

    public void setRulesEvaluated(int rulesEvaluated) {
        this.rulesEvaluated = rulesEvaluated;
    }

    public int getRulesPassed() {
        return rulesPassed;
    }

    public void setRulesPassed(int rulesPassed) {
        this.rulesPassed = rulesPassed;
    }

    public int getRulesFailed() {
        return rulesFailed;
    }

    public void setRulesFailed(int rulesFailed) {
        this.rulesFailed = rulesFailed;
    }

    public int getRulesNotEvaluable() {
        return rulesNotEvaluable;
    }

    public void setRulesNotEvaluable(int rulesNotEvaluable) {
        this.rulesNotEvaluable = rulesNotEvaluable;
    }

    public int getRulesNotApplicable() {
        return rulesNotApplicable;
    }

    public void setRulesNotApplicable(int rulesNotApplicable) {
        this.rulesNotApplicable = rulesNotApplicable;
    }

    public int getRulesErrored() {
        return rulesErrored;
    }

    public void setRulesErrored(int rulesErrored) {
        this.rulesErrored = rulesErrored;
    }
}
