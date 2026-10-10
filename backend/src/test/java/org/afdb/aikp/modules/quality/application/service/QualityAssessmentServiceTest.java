package org.afdb.aikp.modules.quality.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.Map;

import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionId;
import org.afdb.aikp.modules.quality.domain.enums.QualityDimension;
import org.afdb.aikp.modules.quality.domain.enums.QualityDimensionStatus;
import org.afdb.aikp.modules.quality.domain.enums.QualityFindingStatus;
import org.afdb.aikp.modules.quality.domain.enums.QualityLevel;
import org.afdb.aikp.modules.quality.domain.enums.QualityRuleProvenance;
import org.afdb.aikp.modules.quality.domain.enums.QualityRuleType;
import org.afdb.aikp.modules.quality.domain.enums.QualityRunStatus;
import org.afdb.aikp.modules.quality.domain.enums.QualityRunTrigger;
import org.afdb.aikp.modules.quality.domain.enums.QualitySeverity;
import org.afdb.aikp.modules.quality.domain.model.QualityAssessment;
import org.afdb.aikp.modules.quality.domain.model.QualityRun;
import org.afdb.aikp.modules.quality.domain.model.RuleEvaluation;
import org.afdb.aikp.modules.quality.domain.repository.QualityFindingRepository;
import org.afdb.aikp.modules.quality.domain.repository.QualityRunRepository;
import org.afdb.aikp.modules.quality.domain.valueobject.QualityRunId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QualityAssessmentServiceTest {

    private QualityAssessmentService service;

    @BeforeEach
    void setUp() {
        service = new QualityAssessmentService(
                mock(QualityRunRepository.class),
                mock(QualityFindingRepository.class));
    }

    @Test
    void shouldCountPassedRulesEvenWhenTheyDoNotCreateFindings() {

        QualityRun run = newRun();

        addEvaluation(run, passed(
                "PWB-001",
                QualityRuleType.STRUCTURAL,
                QualitySeverity.CRITICAL));

        addEvaluation(run, passed(
                "PWB-003",
                QualityRuleType.LOGICAL,
                QualitySeverity.MAJOR));

        run.complete();

        QualityAssessment assessment =
                service.assess(run, List.of());

        var dimension = assessment.getDimensions()
                .stream()
                .filter(d -> d.getDimension()
                        == QualityDimension.INTERNAL_CONSISTENCY)
                .findFirst()
                .orElseThrow(() -> new AssertionError(
                        "INTERNAL_CONSISTENCY dimension not found"));

        assertThat(dimension.getRulesEvaluated()).isEqualTo(2);
        assertThat(dimension.getFindingsCount()).isZero();
        assertThat(dimension.getStatus())
                .isEqualTo(QualityDimensionStatus.EVALUATED);
    }

    @Test
    void shouldKeepCriticalFindingFromBecomingBusinessValidation() {

        QualityRun run = newRun();

        RuleEvaluation evaluation = RuleEvaluation.failed(
                "PWB-002",
                QualityRuleType.RECONCILIATION,
                QualityRuleProvenance.SOURCE_DEFINED,
                QualitySeverity.CRITICAL,
                "Generation reconciliation",
                "Reconciliation failed",
                "B001 differs from the sum of B002-B005.",
                "Review the underlying values.",
                2025,
                List.of("B001", "B002", "B003", "B004", "B005"),
                Map.of());

        addEvaluation(run, evaluation);

        var finding = org.afdb.aikp.modules.quality.domain.model.QualityFinding
                .from(run.getId(), evaluation);

        run.complete();

        QualityAssessment assessment =
                service.assess(run, List.of(finding));

        assertThat(assessment.getOverallLevel())
                .isIn(QualityLevel.FAIR, QualityLevel.POOR);

        assertThat(assessment.getSignificantFindings())
                .hasSize(1)
                .first()
                .extracting(
                        org.afdb.aikp.modules.quality.domain.model.QualityFinding
                                ::getRuleCode)
                .isEqualTo("PWB-002");

        assertThat(assessment.isAnalysisComplete())
                .isTrue();
    }

    @Test
    void shouldNotTreatNotEvaluableAsFailed() {

        QualityRun run = newRun();

        RuleEvaluation evaluation = RuleEvaluation.notEvaluable(
                "PWC-001",
                QualityRuleType.RECONCILIATION,
                QualityRuleProvenance.PROPOSED,
                QualitySeverity.MAJOR,
                "Capacity reconciliation",
                "Required values are unavailable.",
                2025,
                List.of("A214", "A261", "A262"),
                Map.of());

        addEvaluation(run, evaluation);

        run.complete();

        QualityAssessment assessment =
                service.assess(run, List.of());

        assertThat(assessment.getDimensions())
                .anySatisfy(dimension -> {
                    if (dimension.getDimension()
                            == QualityDimension.RECONCILIATION) {
                        assertThat(dimension.getRulesEvaluated())
                                .isEqualTo(1);
                        assertThat(dimension.getFindingsCount())
                                .isZero();
                        assertThat(dimension.getStatus())
                                .isEqualTo(
                                        QualityDimensionStatus.PARTIALLY_EVALUATED);
                    }
                });
    }

    @Test
    void shouldIgnoreNotApplicableRuleForDimensionEvaluation() {

        QualityRun run = newRun();

        RuleEvaluation evaluation = RuleEvaluation.notApplicable(
                "TEST-NA",
                QualityRuleType.TEMPORAL,
                QualityRuleProvenance.PROPOSED,
                QualitySeverity.INFO,
                "Temporal rule",
                "Rule is not applicable.");

        addEvaluation(run, evaluation);

        run.complete();

        QualityAssessment assessment =
                service.assess(run, List.of());

        assertThat(assessment.getDimensions())
                .anySatisfy(dimension -> {
                    if (dimension.getDimension()
                            == QualityDimension.TEMPORAL_CONSISTENCY) {
                        assertThat(dimension.getRulesEvaluated())
                                .isZero();
                        assertThat(dimension.getStatus())
                                .isEqualTo(
                                        QualityDimensionStatus.NOT_EVALUABLE);
                    }
                });
    }

    @Test
    void shouldMarkAnalysisIncompleteWhenRunContainsErrors() {

        QualityRun run = newRun();

        RuleEvaluation evaluation = RuleEvaluation.error(
                "FG-001",
                QualityRuleType.RECONCILIATION,
                QualityRuleProvenance.SOURCE_DEFINED,
                QualitySeverity.CRITICAL,
                "Financial reconciliation",
                "Unexpected evaluation error.",
                2025,
                List.of("F132", "F133", "F134", "F135"));

        addEvaluation(run, evaluation);

        var finding = org.afdb.aikp.modules.quality.domain.model.QualityFinding
                .from(run.getId(), evaluation);

        run.complete();

        QualityAssessment assessment =
                service.assess(run, List.of(finding));

        assertThat(assessment.isAnalysisComplete())
                .isFalse();

        assertThat(assessment.getSignificantFindings())
                .isEmpty();

        assertThat(assessment.getDimensions())
                .anySatisfy(dimension -> {
                    if (dimension.getDimension()
                            == QualityDimension.RECONCILIATION) {

                        assertThat(dimension.getStatus())
                                .isEqualTo(
                                        QualityDimensionStatus.PARTIALLY_EVALUATED);

                        assertThat(dimension.getRulesEvaluated())
                                .isEqualTo(1);

                        assertThat(dimension.getAffectedRuleCodes())
                                .containsExactly("FG-001");
                    }
                });
    }

    @Test
    void shouldMapPwARulesToTheirSpecificDimensions() {

        QualityRun run = newRun();

        addEvaluation(run, passed(
                "PWA-001",
                QualityRuleType.STRUCTURAL,
                QualitySeverity.MAJOR));

        addEvaluation(run, passed(
                "PWA-006",
                QualityRuleType.LOGICAL,
                QualitySeverity.MAJOR));

        run.complete();

        QualityAssessment assessment =
                service.assess(run, List.of());

        assertThat(assessment.getDimensions())
                .anySatisfy(dimension -> {
                    if (dimension.getDimension()
                            == QualityDimension.COMPLETENESS) {
                        assertThat(dimension.getRulesEvaluated())
                                .isEqualTo(1);
                    }
                });

        assertThat(assessment.getDimensions())
                .anySatisfy(dimension -> {
                    if (dimension.getDimension()
                            == QualityDimension
                                    .INSTITUTIONAL_REGULATORY_CONSISTENCY) {
                        assertThat(dimension.getRulesEvaluated())
                                .isEqualTo(1);
                    }
                });
    }

    private QualityRun newRun() {

        QualityRun run = QualityRun.start(
                QualityRunId.generate(),
                DataCollectionId.generate(),
                QualityRunTrigger.MANUAL);

        return run;
    }

    private void addEvaluation(QualityRun run, RuleEvaluation evaluation) {
        run.addEvaluation(evaluation);
        run.recordEvaluation(evaluation.status());
    }

    private RuleEvaluation passed(
            String code,
            QualityRuleType type,
            QualitySeverity severity) {

        return RuleEvaluation.passed(
                code,
                type,
                QualityRuleProvenance.PROPOSED,
                severity,
                code + " test",
                2025,
                List.of(),
                Map.of());
    }
}
