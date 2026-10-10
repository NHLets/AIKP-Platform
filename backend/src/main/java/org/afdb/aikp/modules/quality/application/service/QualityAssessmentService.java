package org.afdb.aikp.modules.quality.application.service;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionId;
import org.afdb.aikp.modules.quality.domain.enums.QualityDimension;
import org.afdb.aikp.modules.quality.domain.enums.QualityDimensionStatus;
import org.afdb.aikp.modules.quality.domain.enums.QualityFindingStatus;
import org.afdb.aikp.modules.quality.domain.enums.QualityLevel;
import org.afdb.aikp.modules.quality.domain.enums.QualityRuleType;
import org.afdb.aikp.modules.quality.domain.enums.QualityRunStatus;
import org.afdb.aikp.modules.quality.domain.enums.QualitySeverity;
import org.afdb.aikp.modules.quality.domain.model.QualityAssessment;
import org.afdb.aikp.modules.quality.domain.model.QualityDimensionAssessment;
import org.afdb.aikp.modules.quality.domain.model.QualityFinding;
import org.afdb.aikp.modules.quality.domain.model.QualityRun;
import org.afdb.aikp.modules.quality.domain.model.RuleEvaluation;
import org.afdb.aikp.modules.quality.domain.repository.QualityFindingRepository;
import org.afdb.aikp.modules.quality.domain.repository.QualityRunRepository;
import org.springframework.stereotype.Service;

@Service
public class QualityAssessmentService {

    private final QualityRunRepository qualityRunRepository;
    private final QualityFindingRepository qualityFindingRepository;

    public QualityAssessmentService(
            QualityRunRepository qualityRunRepository,
            QualityFindingRepository qualityFindingRepository) {
        this.qualityRunRepository = qualityRunRepository;
        this.qualityFindingRepository = qualityFindingRepository;
    }

    /**
     * Builds the current analytical quality assessment from the latest
     * completed quality run.
     *
     * This service is advisory only:
     * - it does not change DataCollection status;
     * - it does not validate or reject a DataCollection;
     * - it does not modify observations.
     */
    public QualityAssessment assess(DataCollectionId dataCollectionId) {

        QualityRun run = qualityRunRepository
                .findLatestByDataCollectionId(dataCollectionId)
                .orElseThrow(() -> new IllegalStateException(
                        "No quality run found for data collection "
                                + dataCollectionId.getValue()));

        if (run.getStatus() != QualityRunStatus.COMPLETED) {
            throw new IllegalStateException(
                    "Latest quality run is not completed: "
                            + run.getId().getValue());
        }

        List<QualityFinding> findings =
                qualityFindingRepository.findByQualityRunId(run.getId());

        return assess(run, findings);
    }

    /**
     * Pure assessment operation useful for unit tests and for callers that
     * already have the run and its findings.
     */
    public QualityAssessment assess(
            QualityRun run,
            List<QualityFinding> findings) {

        Map<QualityDimension, DimensionAccumulator> accumulators =
                createAccumulators();

        /*
         * Dimensions are built from ALL technical rule evaluations.
         * Findings remain user-facing anomalies and are added separately.
         *
         * This distinction is essential:
         * - PASSED rules must contribute to an evaluated dimension;
         * - NOT_EVALUABLE rules must make the dimension partial;
         * - NOT_APPLICABLE rules must not be treated as failures;
         * - FAILED / ERROR findings are attached separately.
         */
        for (RuleEvaluation evaluation : run.getEvaluations()) {
            QualityDimension dimension = resolveDimension(evaluation);

            if (dimension == null) {
                continue;
            }

            accumulators.get(dimension).addEvaluation(evaluation);
        }

        for (QualityFinding finding : findings) {
            QualityDimension dimension = resolveDimension(finding);

            if (dimension == null) {
                continue;
            }

            accumulators.get(dimension).addFinding(finding);
        }

        List<QualityDimensionAssessment> dimensions =
                new ArrayList<>();

        for (QualityDimension dimension : QualityDimension.values()) {
            DimensionAccumulator accumulator = accumulators.get(dimension);

            dimensions.add(accumulator.toAssessment(dimension));
        }

        boolean analysisComplete = run.getRulesErrored() == 0;

        QualityLevel overallLevel =
                determineOverallLevel(run, dimensions, findings);

        List<QualityFinding> significantFindings =
                findings.stream()
                        .filter(this::isSignificant)
                        .toList();

        String summary = buildSummary(
                run,
                dimensions,
                findings,
                overallLevel,
                analysisComplete);

        return QualityAssessment.create(
                run.getDataCollectionId(),
                run.getId(),
                overallLevel,
                analysisComplete,
                dimensions,
                significantFindings,
                summary);
    }

    /**
     * Maps analytical rule families to quality dimensions.
     *
     * The mapping is intentionally explicit rather than inferred from
     * arbitrary rule metadata.
     */
    private QualityDimension resolveDimension(RuleEvaluation evaluation) {

        String code = evaluation.ruleCode();

        /*
         * PW_A completeness is a structural quality issue.
         */
        if ("PWA-001".equals(code)) {
            return QualityDimension.COMPLETENESS;
        }

        /*
         * PW_A institutional/regulatory consistency.
         */
        if ("PWA-006".equals(code)) {
            return QualityDimension.INSTITUTIONAL_REGULATORY_CONSISTENCY;
        }

        if (evaluation.ruleType() == QualityRuleType.LOGICAL) {
            return QualityDimension.INTERNAL_CONSISTENCY;
        }

        if (evaluation.ruleType() == QualityRuleType.RECONCILIATION) {
            return QualityDimension.RECONCILIATION;
        }

        if (evaluation.ruleType() == QualityRuleType.STRUCTURAL) {
            return QualityDimension.INTERNAL_CONSISTENCY;
        }

        if (evaluation.ruleType() == QualityRuleType.MATHEMATICAL) {
            return QualityDimension.MATHEMATICAL_CONSISTENCY;
        }

        if (evaluation.ruleType() == QualityRuleType.TEMPORAL) {
            return QualityDimension.TEMPORAL_CONSISTENCY;
        }

        if (evaluation.ruleType() == QualityRuleType.STATISTICAL) {
            return QualityDimension.MATHEMATICAL_CONSISTENCY;
        }

        if (evaluation.ruleType() == QualityRuleType.CROSS_TEMPLATE) {
            return QualityDimension.CROSS_TEMPLATE_CONSISTENCY;
        }

        return null;
    }

    private QualityDimension resolveDimension(QualityFinding finding) {

        String code = finding.getRuleCode();

        /*
         * PW_A completeness is a structural quality issue.
         */
        if ("PWA-001".equals(code)) {
            return QualityDimension.COMPLETENESS;
        }

        /*
         * PW_A institutional/regulatory consistency.
         *
         * PWA-006 tests a defined logical relation between institutional
         * variables D037 and D038.
         */
        if ("PWA-006".equals(code)) {
            return QualityDimension.INSTITUTIONAL_REGULATORY_CONSISTENCY;
        }

        /*
         * Logical consistency for all other logical rules.
         */
        if (finding.getRuleType() == QualityRuleType.LOGICAL) {
            return QualityDimension.INTERNAL_CONSISTENCY;
        }

        /*
         * Reconciliation rules represent mathematical/accounting
         * consistency between related variables.
         */
        if (finding.getRuleType() == QualityRuleType.RECONCILIATION) {
            return QualityDimension.RECONCILIATION;
        }

        /*
         * Structural rules cover completeness and structural validity.
         * PWA-001 is handled explicitly above.
         */
        if (finding.getRuleType() == QualityRuleType.STRUCTURAL) {
            return QualityDimension.INTERNAL_CONSISTENCY;
        }

        if (finding.getRuleType() == QualityRuleType.MATHEMATICAL) {
            return QualityDimension.MATHEMATICAL_CONSISTENCY;
        }

        if (finding.getRuleType() == QualityRuleType.TEMPORAL) {
            return QualityDimension.TEMPORAL_CONSISTENCY;
        }

        if (finding.getRuleType() == QualityRuleType.STATISTICAL) {
            return QualityDimension.MATHEMATICAL_CONSISTENCY;
        }

        if (finding.getRuleType() == QualityRuleType.CROSS_TEMPLATE) {
            return QualityDimension.CROSS_TEMPLATE_CONSISTENCY;
        }

        return null;
    }

    private Map<QualityDimension, DimensionAccumulator> createAccumulators() {
        Map<QualityDimension, DimensionAccumulator> result =
                new EnumMap<>(QualityDimension.class);

        for (QualityDimension dimension : QualityDimension.values()) {
            result.put(dimension, new DimensionAccumulator());
        }

        return result;
    }

    private QualityLevel determineOverallLevel(
            QualityRun run,
            List<QualityDimensionAssessment> dimensions,
            List<QualityFinding> findings) {

        if (run.getRulesErrored() > 0) {
            /*
             * Technical execution errors make the analysis incomplete.
             * They are not interpreted as poor data quality.
             */
            return levelFromDimensions(dimensions);
        }

        long criticalCount = findings.stream()
                .filter(f -> f.getStatus() == QualityFindingStatus.FAILED)
                .filter(f -> f.getSeverity() == QualitySeverity.CRITICAL)
                .count();

        long majorCount = findings.stream()
                .filter(f -> f.getStatus() == QualityFindingStatus.FAILED)
                .filter(f -> f.getSeverity() == QualitySeverity.MAJOR)
                .count();

        long warningCount = findings.stream()
                .filter(f -> f.getStatus() == QualityFindingStatus.FAILED)
                .filter(f -> f.getSeverity() == QualitySeverity.WARNING)
                .count();

        QualityLevel baseLevel = levelFromDimensions(dimensions);

        /*
         * Severity dominance rules:
         *
         * CRITICAL => maximum FAIR
         * multiple CRITICAL => POOR
         * multiple MAJOR => maximum FAIR
         * one MAJOR => maximum GOOD
         */
        if (criticalCount >= 2) {
            return QualityLevel.POOR;
        }

        if (criticalCount == 1) {
            return minLevel(baseLevel, QualityLevel.FAIR);
        }

        if (majorCount >= 3) {
            return minLevel(baseLevel, QualityLevel.WEAK);
        }

        if (majorCount >= 2) {
            return minLevel(baseLevel, QualityLevel.FAIR);
        }

        if (majorCount == 1) {
            return minLevel(baseLevel, QualityLevel.GOOD);
        }

        if (warningCount >= 3) {
            return minLevel(baseLevel, QualityLevel.GOOD);
        }

        return baseLevel;
    }

    private QualityLevel levelFromDimensions(
            List<QualityDimensionAssessment> dimensions) {

        List<QualityDimensionAssessment> evaluated =
                dimensions.stream()
                        .filter(d -> d.getStatus()
                                != QualityDimensionStatus.NOT_EVALUABLE)
                        .toList();

        if (evaluated.isEmpty()) {
            return QualityLevel.FAIR;
        }

        QualityLevel worst = QualityLevel.VERY_GOOD;

        for (QualityDimensionAssessment dimension : evaluated) {
            worst = minLevel(worst, dimension.getLevel());
        }

        return worst;
    }

    /**
     * QualityLevel ordering is from best to worst.
     */
    private QualityLevel minLevel(
            QualityLevel left,
            QualityLevel right) {

        return rank(left) >= rank(right) ? left : right;
    }

    private int rank(QualityLevel level) {
        return switch (level) {
            case VERY_GOOD -> 0;
            case GOOD -> 1;
            case FAIR -> 2;
            case WEAK -> 3;
            case POOR -> 4;
        };
    }

    private boolean isSignificant(QualityFinding finding) {
        return finding.getStatus() == QualityFindingStatus.FAILED
                && (finding.getSeverity() == QualitySeverity.CRITICAL
                    || finding.getSeverity() == QualitySeverity.MAJOR);
    }

    private String buildSummary(
            QualityRun run,
            List<QualityDimensionAssessment> dimensions,
            List<QualityFinding> findings,
            QualityLevel overallLevel,
            boolean analysisComplete) {

        long critical = findings.stream()
                .filter(f -> f.getStatus() == QualityFindingStatus.FAILED)
                .filter(f -> f.getSeverity() == QualitySeverity.CRITICAL)
                .count();

        long major = findings.stream()
                .filter(f -> f.getStatus() == QualityFindingStatus.FAILED)
                .filter(f -> f.getSeverity() == QualitySeverity.MAJOR)
                .count();

        long warning = findings.stream()
                .filter(f -> f.getStatus() == QualityFindingStatus.FAILED)
                .filter(f -> f.getSeverity() == QualitySeverity.WARNING)
                .count();

        long notEvaluable = dimensions.stream()
                .filter(d -> d.getStatus()
                        == QualityDimensionStatus.NOT_EVALUABLE)
                .count();

        StringBuilder summary = new StringBuilder();

        summary.append("Overall quality assessment: ")
                .append(formatLevel(overallLevel))
                .append(".");

        if (critical > 0) {
            summary.append(" ")
                    .append(critical)
                    .append(" critical finding")
                    .append(critical > 1 ? "s" : "")
                    .append(" require")
                    .append(critical == 1 ? "s" : "")
                    .append(" particular attention.");
        }

        if (major > 0) {
            summary.append(" ")
                    .append(major)
                    .append(" major finding")
                    .append(major > 1 ? "s" : "")
                    .append(" were identified.");
        }

        if (warning > 0) {
            summary.append(" ")
                    .append(warning)
                    .append(" warning")
                    .append(warning > 1 ? "s" : "")
                    .append(" were identified.");
        }

        if (notEvaluable > 0) {
            summary.append(" ")
                    .append(notEvaluable)
                    .append(" dimension")
                    .append(notEvaluable > 1 ? "s" : "")
                    .append(" could not be fully evaluated.");
        }

        if (!analysisComplete) {
            summary.append(
                    " The quality analysis is incomplete because "
                    + "one or more quality rules encountered an execution error.");
        }

        summary.append(
                " This assessment is advisory and does not determine "
                + "the business validation status of the data collection.");

        return summary.toString();
    }

    private String formatLevel(QualityLevel level) {
        return switch (level) {
            case VERY_GOOD -> "Very Good";
            case GOOD -> "Good";
            case FAIR -> "Fair";
            case WEAK -> "Weak";
            case POOR -> "Poor";
        };
    }

    private static final class DimensionAccumulator {

        private int rulesEvaluated;
        private int findingsCount;
        private int failedCount;
        private int notEvaluableCount;
        private int errorCount;

        private final Set<String> affectedRuleCodes =
                new LinkedHashSet<>();

        private final List<QualityFinding> findings = new ArrayList<>();

        void addEvaluation(RuleEvaluation evaluation) {

            /*
             * NOT_APPLICABLE means that the rule does not belong to the
             * applicable analytical scope. It must not count as an
             * evaluated rule.
             */
            if (evaluation.status()
                    == QualityFindingStatus.NOT_APPLICABLE) {
                return;
            }

            rulesEvaluated++;
            affectedRuleCodes.add(evaluation.ruleCode());

            if (evaluation.status()
                    == QualityFindingStatus.NOT_EVALUABLE) {
                notEvaluableCount++;
            }

            if (evaluation.status()
                    == QualityFindingStatus.ERROR) {
                errorCount++;
            }
        }

        void addFinding(QualityFinding finding) {

            findingsCount++;
            affectedRuleCodes.add(finding.getRuleCode());
            findings.add(finding);

            if (finding.getStatus()
                    == QualityFindingStatus.FAILED) {
                failedCount++;
            }

            if (finding.getStatus()
                    == QualityFindingStatus.NOT_EVALUABLE) {
                notEvaluableCount++;
            }

            if (finding.getStatus()
                    == QualityFindingStatus.ERROR) {
                errorCount++;
            }
        }

        QualityDimensionAssessment toAssessment(
                QualityDimension dimension) {

            /*
             * No applicable/evaluable rule exists for this dimension.
             */
            if (rulesEvaluated == 0) {
                return QualityDimensionAssessment.notEvaluable(
                        dimension,
                        "No applicable quality rule was evaluated for this dimension.");
            }

            QualityDimensionStatus status;

            if (errorCount > 0 || notEvaluableCount > 0) {
                status = QualityDimensionStatus.PARTIALLY_EVALUATED;
            } else {
                status = QualityDimensionStatus.EVALUATED;
            }

            QualityLevel level = determineLevel();

            String explanation = buildExplanation();

            if (status == QualityDimensionStatus.PARTIALLY_EVALUATED) {
                return QualityDimensionAssessment.partiallyEvaluated(
                        dimension,
                        level,
                        rulesEvaluated,
                        findingsCount,
                        new ArrayList<>(affectedRuleCodes),
                        explanation);
            }

            return QualityDimensionAssessment.evaluated(
                    dimension,
                    level,
                    rulesEvaluated,
                    findingsCount,
                    new ArrayList<>(affectedRuleCodes),
                    explanation);
        }

        private QualityLevel determineLevel() {

            long critical = findings.stream()
                    .filter(f -> f.getStatus()
                            == QualityFindingStatus.FAILED)
                    .filter(f -> f.getSeverity()
                            == QualitySeverity.CRITICAL)
                    .count();

            long major = findings.stream()
                    .filter(f -> f.getStatus()
                            == QualityFindingStatus.FAILED)
                    .filter(f -> f.getSeverity()
                            == QualitySeverity.MAJOR)
                    .count();

            long warning = findings.stream()
                    .filter(f -> f.getStatus()
                            == QualityFindingStatus.FAILED)
                    .filter(f -> f.getSeverity()
                            == QualitySeverity.WARNING)
                    .count();

            if (critical >= 2) {
                return QualityLevel.POOR;
            }

            if (critical == 1) {
                return QualityLevel.FAIR;
            }

            if (major >= 3) {
                return QualityLevel.WEAK;
            }

            if (major >= 2) {
                return QualityLevel.FAIR;
            }

            if (major == 1) {
                return QualityLevel.GOOD;
            }

            if (warning > 0) {
                return QualityLevel.GOOD;
            }

            return QualityLevel.VERY_GOOD;
        }

        private String buildExplanation() {

            if (failedCount == 0
                    && notEvaluableCount == 0
                    && errorCount == 0) {

                return rulesEvaluated
                        + " quality rule"
                        + (rulesEvaluated > 1 ? "s were" : " was")
                        + " evaluated successfully with no finding.";
            }

            StringBuilder explanation = new StringBuilder();

            if (failedCount > 0) {
                explanation.append(failedCount)
                        .append(" failed quality rule")
                        .append(failedCount > 1 ? "s" : "")
                        .append(".");
            }

            if (notEvaluableCount > 0) {
                explanation.append(" ")
                        .append(notEvaluableCount)
                        .append(" rule")
                        .append(notEvaluableCount > 1 ? "s" : "")
                        .append(" could not be evaluated.");
            }

            if (errorCount > 0) {
                explanation.append(" ")
                        .append(errorCount)
                        .append(" rule")
                        .append(errorCount > 1 ? "s" : "")
                        .append(" encountered an execution error.");
            }

            return explanation.toString().trim();
        }
    }
}
