package org.afdb.aikp.modules.quality.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionId;
import org.afdb.aikp.modules.quality.domain.model.QualityRun;
import org.afdb.aikp.modules.quality.domain.repository.QualityRunRepository;
import org.afdb.aikp.modules.quality.domain.valueobject.QualityRunId;
import org.afdb.aikp.modules.quality.infrastructure.persistence.entity.QualityRunEntity;
import org.afdb.aikp.modules.quality.infrastructure.persistence.mapper.QualityRunPersistenceMapper;
import org.afdb.aikp.modules.quality.infrastructure.persistence.repository.QualityRunJpaRepository;

@Repository
public class QualityRunRepositoryAdapter
        implements QualityRunRepository {

    private final QualityRunJpaRepository repository;
    private final QualityRunPersistenceMapper mapper;

    public QualityRunRepositoryAdapter(
            QualityRunJpaRepository repository,
            QualityRunPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public QualityRun save(QualityRun run) {
        QualityRunEntity entity = mapper.toEntity(run);
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public void deleteById(QualityRunId id) {
        repository.deleteById(id.getValue());
    }

    @Override
    public Optional<QualityRun> findById(QualityRunId id) {
        return repository.findById(id.getValue())
                .map(mapper::toDomain);
    }

    @Override
    public List<QualityRun> findByDataCollectionId(
            DataCollectionId dataCollectionId) {

        return repository
                .findByDataCollectionIdOrderByStartedAtDesc(
                        dataCollectionId.getValue())
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<QualityRun> findLatestByDataCollectionId(
            DataCollectionId dataCollectionId) {

        return repository
                .findFirstByDataCollectionIdOrderByStartedAtDesc(
                        dataCollectionId.getValue())
                .map(mapper::toDomain);
    }
}
