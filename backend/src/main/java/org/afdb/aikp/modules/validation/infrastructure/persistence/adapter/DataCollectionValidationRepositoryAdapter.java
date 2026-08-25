package org.afdb.aikp.modules.validation.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionId;
import org.afdb.aikp.modules.validation.domain.model.DataCollectionValidation;
import org.afdb.aikp.modules.validation.domain.repository.DataCollectionValidationRepository;
import org.afdb.aikp.modules.validation.domain.valueobject.DataCollectionValidationId;

import org.afdb.aikp.modules.validation.infrastructure.persistence.entity.DataCollectionValidationEntity;
import org.afdb.aikp.modules.validation.infrastructure.persistence.mapper.DataCollectionValidationPersistenceMapper;
import org.afdb.aikp.modules.validation.infrastructure.persistence.repository.DataCollectionValidationJpaRepository;

import org.springframework.stereotype.Repository;

/**
 * Persistence adapter implementing the
 * DataCollectionValidationRepository domain contract.
 */
@Repository
public class DataCollectionValidationRepositoryAdapter
        implements DataCollectionValidationRepository {

    private final DataCollectionValidationJpaRepository
            validationJpaRepository;

    private final DataCollectionValidationPersistenceMapper
            mapper;

    public DataCollectionValidationRepositoryAdapter(
            DataCollectionValidationJpaRepository
                    validationJpaRepository,
            DataCollectionValidationPersistenceMapper mapper) {

        this.validationJpaRepository =
                validationJpaRepository;

        this.mapper = mapper;
    }

    @Override
    public DataCollectionValidation save(
            DataCollectionValidation validation) {

        DataCollectionValidationEntity entity =
                mapper.toEntity(validation);

        DataCollectionValidationEntity savedEntity =
                validationJpaRepository.save(entity);

        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<DataCollectionValidation> findById(
            DataCollectionValidationId id) {

        return validationJpaRepository
                .findById(id.getValue())
                .map(mapper::toDomain);
    }

    @Override
    public List<DataCollectionValidation>
            findByDataCollectionId(
                    DataCollectionId dataCollectionId) {

        return validationJpaRepository
                .findByDataCollectionId(
                        dataCollectionId.getValue())
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsById(
            DataCollectionValidationId id) {

        return validationJpaRepository
                .existsById(id.getValue());
    }

    @Override
    public void delete(
            DataCollectionValidation validation) {

        validationJpaRepository.deleteById(
                validation
                        .getDataCollectionValidationId()
                        .getValue());
    }
}
