package org.afdb.aikp.modules.quality.infrastructure.persistence.adapter;

import java.util.List;

import org.springframework.stereotype.Repository;

import org.afdb.aikp.modules.quality.domain.model.RuleEvaluation;
import org.afdb.aikp.modules.quality.domain.repository.QualityRuleEvaluationRepository;
import org.afdb.aikp.modules.quality.domain.valueobject.QualityRunId;
import org.afdb.aikp.modules.quality.infrastructure.persistence.entity.QualityRuleEvaluationEntity;
import org.afdb.aikp.modules.quality.infrastructure.persistence.mapper.QualityRuleEvaluationPersistenceMapper;
import org.afdb.aikp.modules.quality.infrastructure.persistence.repository.QualityRuleEvaluationJpaRepository;

@Repository
public class QualityRuleEvaluationRepositoryAdapter
        implements QualityRuleEvaluationRepository {

    private final QualityRuleEvaluationJpaRepository repository;
    private final QualityRuleEvaluationPersistenceMapper mapper;

    public QualityRuleEvaluationRepositoryAdapter(
            QualityRuleEvaluationJpaRepository repository,
            QualityRuleEvaluationPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void saveAll(
            QualityRunId qualityRunId,
            List<RuleEvaluation> evaluations) {

        List<QualityRuleEvaluationEntity> entities =
                evaluations.stream()
                        .map(evaluation ->
                                mapper.toEntity(qualityRunId, evaluation))
                        .toList();

        repository.saveAll(entities);
    }

    @Override
    public List<RuleEvaluation> findByQualityRunId(
            QualityRunId qualityRunId) {

        return repository
                .findByQualityRunIdOrderByRuleCodeAsc(
                        qualityRunId.getValue())
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void deleteByQualityRunId(QualityRunId qualityRunId) {
        repository.deleteByQualityRunId(qualityRunId.getValue());
    }
}
