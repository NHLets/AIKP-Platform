package org.afdb.aikp.modules.quality.domain.model;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionId;
import org.afdb.aikp.modules.quality.domain.enums.QualityFindingStatus;
import org.afdb.aikp.modules.quality.domain.enums.QualityRunStatus;
import org.afdb.aikp.modules.quality.domain.enums.QualityRunTrigger;
import org.afdb.aikp.modules.quality.domain.valueobject.QualityRunId;

public final class QualityRun {

    private final QualityRunId id;
    private final DataCollectionId dataCollectionId;
    private final QualityRunTrigger trigger;
    private final OffsetDateTime startedAt;

    private QualityRunStatus status;
    private OffsetDateTime completedAt;
    private int rulesEvaluated;
    private int rulesPassed;
    private int rulesFailed;
    private int rulesNotEvaluable;
    private int rulesNotApplicable;
    private int rulesErrored;

    /**
     * Technical result of every rule evaluated during this run.
     * These evaluations are kept separately from user-facing findings:
     * PASSED evaluations do not create QualityFinding instances.
     */
    private final List<RuleEvaluation> evaluations = new ArrayList<>();

    private QualityRun(
            QualityRunId id,
            DataCollectionId dataCollectionId,
            QualityRunTrigger trigger,
            OffsetDateTime startedAt,
            QualityRunStatus status) {
        this.id = Objects.requireNonNull(id);
        this.dataCollectionId = Objects.requireNonNull(dataCollectionId);
        this.trigger = Objects.requireNonNull(trigger);
        this.startedAt = Objects.requireNonNull(startedAt);
        this.status = Objects.requireNonNull(status);
    }

    public static QualityRun start(
            QualityRunId id,
            DataCollectionId dataCollectionId,
            QualityRunTrigger trigger) {
        return new QualityRun(
                id, dataCollectionId, trigger,
                OffsetDateTime.now(), QualityRunStatus.RUNNING);
    }

    public static QualityRun reconstitute(
            QualityRunId id,
            DataCollectionId dataCollectionId,
            QualityRunTrigger trigger,
            OffsetDateTime startedAt,
            QualityRunStatus status,
            OffsetDateTime completedAt,
            int rulesEvaluated,
            int rulesPassed,
            int rulesFailed,
            int rulesNotEvaluable,
            int rulesNotApplicable,
            int rulesErrored,
            List<RuleEvaluation> evaluations) {

        QualityRun run = new QualityRun(
                id,
                dataCollectionId,
                trigger,
                startedAt,
                status);

        run.completedAt = completedAt;
        run.rulesEvaluated = rulesEvaluated;
        run.rulesPassed = rulesPassed;
        run.rulesFailed = rulesFailed;
        run.rulesNotEvaluable = rulesNotEvaluable;
        run.rulesNotApplicable = rulesNotApplicable;
        run.rulesErrored = rulesErrored;

        if (evaluations != null) {
            run.evaluations.addAll(evaluations);
        }

        return run;
    }

    public void recordEvaluation(QualityFindingStatus result) {
        if (status != QualityRunStatus.RUNNING) {
            throw new IllegalStateException("Quality run is not running.");
        }

        rulesEvaluated++;

        switch (result) {
            case PASSED -> rulesPassed++;
            case FAILED -> rulesFailed++;
            case NOT_EVALUABLE -> rulesNotEvaluable++;
            case NOT_APPLICABLE -> rulesNotApplicable++;
            case ERROR -> rulesErrored++;
        }
    }

    public void complete() {
        ensureRunning();
        status = QualityRunStatus.COMPLETED;
        completedAt = OffsetDateTime.now();
    }

    public void fail() {
        ensureRunning();
        status = QualityRunStatus.FAILED;
        completedAt = OffsetDateTime.now();
    }

    private void ensureRunning() {
        if (status != QualityRunStatus.RUNNING) {
            throw new IllegalStateException("Quality run is not running.");
        }
    }

    /**
     * Records the technical result of one executed quality rule.
     */
    public void addEvaluation(RuleEvaluation evaluation) {
        this.evaluations.add(Objects.requireNonNull(evaluation));
    }

    /**
     * Returns all technical rule evaluations for this run.
     */
    public List<RuleEvaluation> getEvaluations() {
        return Collections.unmodifiableList(evaluations);
    }

    public QualityRunId getId() { return id; }
    public DataCollectionId getDataCollectionId() { return dataCollectionId; }
    public QualityRunTrigger getTrigger() { return trigger; }
    public QualityRunStatus getStatus() { return status; }
    public OffsetDateTime getStartedAt() { return startedAt; }
    public OffsetDateTime getCompletedAt() { return completedAt; }
    public int getRulesEvaluated() { return rulesEvaluated; }
    public int getRulesPassed() { return rulesPassed; }
    public int getRulesFailed() { return rulesFailed; }
    public int getRulesNotEvaluable() { return rulesNotEvaluable; }
    public int getRulesNotApplicable() { return rulesNotApplicable; }
    public int getRulesErrored() { return rulesErrored; }
}
