package org.afdb.aikp.modules.quality.infrastructure.persistence.adapter;

import java.util.List;

import org.springframework.stereotype.Repository;

import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionId;
import org.afdb.aikp.modules.quality.domain.model.QualityFinding;
import org.afdb.aikp.modules.quality.domain.repository.QualityFindingRepository;
import org.afdb.aikp.modules.quality.domain.valueobject.QualityRunId;
import org.afdb.aikp.modules.quality.infrastructure.persistence.entity.QualityFindingEntity;
import org.afdb.aikp.modules.quality.infrastructure.persistence.mapper.QualityFindingPersistenceMapper;
import org.afdb.aikp.modules.quality.infrastructure.persistence.repository.QualityFindingJpaRepository;

@Repository
public class QualityFindingRepositoryAdapter
        implements QualityFindingRepository {

    private final QualityFindingJpaRepository repository;
    private final QualityFindingPersistenceMapper mapper;

    public QualityFindingRepositoryAdapter(
            QualityFindingJpaRepository repository,
            QualityFindingPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public QualityFinding save(QualityFinding finding) {
        QualityFindingEntity entity = mapper.toEntity(finding);
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public List<QualityFinding> findByQualityRunId(
            QualityRunId qualityRunId) {

        return repository
                .findByQualityRunIdOrderBySeverityAsc(
                        qualityRunId.getValue())
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<QualityFinding> findByDataCollectionId(
            DataCollectionId dataCollectionId) {

        return repository
                .findByDataCollectionId(
                        dataCollectionId.getValue())
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void deleteByQualityRunId(QualityRunId qualityRunId) {
        repository.deleteByQualityRunId(qualityRunId.getValue());
    }
}
