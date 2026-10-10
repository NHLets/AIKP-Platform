package org.afdb.aikp.modules.quality.application.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.afdb.aikp.modules.collection.domain.enums.DataCollectionStatus;
import org.afdb.aikp.modules.collection.domain.model.DataCollection;
import org.afdb.aikp.modules.collection.domain.model.DataCollectionObservation;
import org.afdb.aikp.modules.collection.domain.repository.DataCollectionObservationRepository;
import org.afdb.aikp.modules.collection.domain.repository.DataCollectionRepository;
import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionId;
import org.afdb.aikp.modules.questionnaire.domain.model.QuestionnaireVariable;
import org.afdb.aikp.modules.questionnaire.domain.repository.QuestionnaireVariableRepository;
import org.afdb.aikp.modules.quality.domain.enums.QualityRunTrigger;
import org.afdb.aikp.modules.quality.domain.model.QualityFinding;
import org.afdb.aikp.modules.quality.domain.model.QualityRun;
import org.afdb.aikp.modules.quality.domain.model.RuleEvaluation;
import org.afdb.aikp.modules.quality.domain.repository.QualityFindingRepository;
import org.afdb.aikp.modules.quality.domain.repository.QualityRunRepository;
import org.afdb.aikp.modules.quality.domain.service.QualityEvaluationContext;
import org.afdb.aikp.modules.quality.domain.service.QualityRule;
import org.afdb.aikp.modules.quality.domain.service.QuestionnaireVariableResolver;
import org.afdb.aikp.modules.quality.domain.valueobject.QualityRunId;

@Service
public class QualityEngine {

    private final DataCollectionRepository dataCollectionRepository;
    private final DataCollectionObservationRepository observationRepository;
    private final QuestionnaireVariableRepository variableRepository;
    private final QualityRunRepository qualityRunRepository;
    private final QualityFindingRepository qualityFindingRepository;
    private final QuestionnaireVariableResolver variableResolver;
    private final List<QualityRule> rules;

    public QualityEngine(
            DataCollectionRepository dataCollectionRepository,
            DataCollectionObservationRepository observationRepository,
            QuestionnaireVariableRepository variableRepository,
            QualityRunRepository qualityRunRepository,
            QualityFindingRepository qualityFindingRepository,
            QuestionnaireVariableResolver variableResolver,
            List<QualityRule> rules) {
        this.dataCollectionRepository = dataCollectionRepository;
        this.observationRepository = observationRepository;
        this.variableRepository = variableRepository;
        this.qualityRunRepository = qualityRunRepository;
        this.qualityFindingRepository = qualityFindingRepository;
        this.variableResolver = variableResolver;
        this.rules = List.copyOf(rules);
    }

    @Transactional
    public QualityRun execute(
            DataCollectionId dataCollectionId,
            QualityRunTrigger trigger) {

        DataCollection collection = dataCollectionRepository
                .findById(dataCollectionId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Data collection not found: " + dataCollectionId));

        if (collection.getStatus() != DataCollectionStatus.SUBMITTED
                && collection.getStatus() != DataCollectionStatus.VALIDATED) {
            throw new IllegalStateException(
                    "Quality analysis requires a SUBMITTED or VALIDATED DataCollection.");
        }

        QualityRun run = QualityRun.start(
                QualityRunId.generate(), dataCollectionId, trigger);
        qualityRunRepository.save(run);

        try {
            List<QuestionnaireVariable> variables =
                    variableRepository.findByQuestionnaireId(
                            collection.getQuestionnaireId());

            List<DataCollectionObservation> observations =
                    observationRepository.findByDataCollectionId(
                            dataCollectionId);

            Map<String, QuestionnaireVariable> variableMap =
                    variableResolver.resolve(variables);

            Map<String, List<DataCollectionObservation>> observationMap =
                    observations.stream().collect(
                            Collectors.groupingBy(
                                    o -> variableMap.values().stream()
                                            .filter(v -> v.getId().equals(
                                                    o.getQuestionnaireVariableId()))
                                            .map(QuestionnaireVariable::getSeriesCode)
                                            .findFirst()
                                            .orElseThrow(() ->
                                                    new IllegalStateException(
                                                            "Observation references an unknown questionnaire variable."))));

            QualityEvaluationContext context =
                    new QualityEvaluationContext(variableMap, observationMap);

            for (QualityRule rule : rules) {
                if (!rule.appliesTo(context)) {
                    RuleEvaluation evaluation =
                            RuleEvaluation.notApplicable(
                                    rule.code(), rule.type(),
                                    rule.provenance(), rule.severity(),
                                    "Rule not applicable.",
                                    "The rule does not apply to this questionnaire/data context.");
                    run.recordEvaluation(evaluation.status());
                    run.addEvaluation(evaluation);
                    continue;
                }

                RuleEvaluation evaluation;

                try {
                    evaluation = rule.evaluate(context);
                } catch (Exception exception) {
                    evaluation = RuleEvaluation.error(
                            rule.code(), rule.type(),
                            rule.provenance(), rule.severity(),
                            "Quality rule execution error.",
                            exception.getMessage(),
                            null, List.of());
                }

                run.recordEvaluation(evaluation.status());
                run.addEvaluation(evaluation);

                if (evaluation.status() == org.afdb.aikp.modules.quality.domain.enums.QualityFindingStatus.FAILED
                        || evaluation.status() == org.afdb.aikp.modules.quality.domain.enums.QualityFindingStatus.ERROR) {
                    qualityFindingRepository.save(
                            QualityFinding.from(run.getId(), evaluation));
                }
            }

            run.complete();
            QualityRun savedRun = qualityRunRepository.save(run);

            List<QualityRun> runs =
                    qualityRunRepository.findByDataCollectionId(dataCollectionId);

            for (int index = 2; index < runs.size(); index++) {
                qualityRunRepository.deleteById(runs.get(index).getId());
            }

            return savedRun;

        } catch (RuntimeException exception) {
            run.fail();
            qualityRunRepository.save(run);
            throw exception;
        }
    }
}
