package org.afdb.aikp.modules.quality.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionId;
import org.afdb.aikp.modules.quality.domain.model.QualityRun;
import org.afdb.aikp.modules.quality.domain.repository.QualityRunRepository;
import org.afdb.aikp.modules.quality.domain.repository.QualityRuleEvaluationRepository;
import org.afdb.aikp.modules.quality.domain.valueobject.QualityRunId;
import org.afdb.aikp.modules.quality.infrastructure.persistence.entity.QualityRunEntity;
import org.afdb.aikp.modules.quality.infrastructure.persistence.mapper.QualityRunPersistenceMapper;
import org.afdb.aikp.modules.quality.infrastructure.persistence.repository.QualityRunJpaRepository;

@Repository
public class QualityRunRepositoryAdapter
        implements QualityRunRepository {

    private final QualityRunJpaRepository repository;
    private final QualityRunPersistenceMapper mapper;
    private final QualityRuleEvaluationRepository evaluationRepository;

    public QualityRunRepositoryAdapter(
            QualityRunJpaRepository repository,
            QualityRunPersistenceMapper mapper,
            QualityRuleEvaluationRepository evaluationRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.evaluationRepository = evaluationRepository;
    }

    @Override
    public QualityRun save(QualityRun run) {
        QualityRunEntity entity = mapper.toEntity(run);
        QualityRun saved = mapper.toDomain(repository.save(entity));

        evaluationRepository.deleteByQualityRunId(run.getId());
        evaluationRepository.saveAll(
                run.getId(),
                run.getEvaluations());

        return reconstituteWithEvaluations(saved);
    }

    @Override
    public void deleteById(QualityRunId id) {
        repository.deleteById(id.getValue());
    }

    @Override
    public Optional<QualityRun> findById(QualityRunId id) {
        return repository.findById(id.getValue())
                .map(mapper::toDomain)
                .map(this::reconstituteWithEvaluations);
    }

    @Override
    public List<QualityRun> findByDataCollectionId(
            DataCollectionId dataCollectionId) {

        return repository
                .findByDataCollectionIdOrderByStartedAtDesc(
                        dataCollectionId.getValue())
                .stream()
                .map(mapper::toDomain)
                .map(this::reconstituteWithEvaluations)
                .toList();
    }

    @Override
    public Optional<QualityRun> findLatestByDataCollectionId(
            DataCollectionId dataCollectionId) {

        return repository
                .findFirstByDataCollectionIdOrderByStartedAtDesc(
                        dataCollectionId.getValue())
                .map(mapper::toDomain)
                .map(this::reconstituteWithEvaluations);
    }

    private QualityRun reconstituteWithEvaluations(QualityRun run) {
        return QualityRun.reconstitute(
                run.getId(),
                run.getDataCollectionId(),
                run.getTrigger(),
                run.getStartedAt(),
                run.getStatus(),
                run.getCompletedAt(),
                run.getRulesEvaluated(),
                run.getRulesPassed(),
                run.getRulesFailed(),
                run.getRulesNotEvaluable(),
                run.getRulesNotApplicable(),
                run.getRulesErrored(),
                evaluationRepository.findByQualityRunId(run.getId()));
    }
}
