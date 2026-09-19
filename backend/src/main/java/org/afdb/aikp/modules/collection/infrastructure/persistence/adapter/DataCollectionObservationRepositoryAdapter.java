package org.afdb.aikp.modules.collection.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import org.afdb.aikp.modules.collection.domain.model.DataCollectionObservation;
import org.afdb.aikp.modules.collection.domain.repository.DataCollectionObservationRepository;
import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionId;
import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionObservationId;
import org.afdb.aikp.modules.collection.infrastructure.persistence.entity.DataCollectionObservationEntity;
import org.afdb.aikp.modules.collection.infrastructure.persistence.mapper.DataCollectionObservationPersistenceMapper;
import org.afdb.aikp.modules.collection.infrastructure.persistence.repository.DataCollectionObservationJpaRepository;

import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireVariableId;

import org.afdb.aikp.modules.questionnaire.domain.repository.QuestionnaireVariableRepository;
import org.afdb.aikp.modules.questionnaire.domain.enums.QuestionnaireVariableDataType;

/**
 * Persistence adapter implementing the DataCollectionObservation
 * domain repository abstraction.
 *
 * <p>
 * The questionnaire variable data type is resolved through the
 * QuestionnaireVariableRepository when an observation is restored.
 * The type itself is not persisted in the observation table.
 * </p>
 */
@Repository
public class DataCollectionObservationRepositoryAdapter
        implements DataCollectionObservationRepository {

    private final DataCollectionObservationJpaRepository
            jpaRepository;

    private final DataCollectionObservationPersistenceMapper mapper;

    private final QuestionnaireVariableRepository
            questionnaireVariableRepository;

    public DataCollectionObservationRepositoryAdapter(
            DataCollectionObservationJpaRepository jpaRepository,
            DataCollectionObservationPersistenceMapper mapper,
            QuestionnaireVariableRepository questionnaireVariableRepository) {

        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
        this.questionnaireVariableRepository =
                questionnaireVariableRepository;
    }

    @Override
    public DataCollectionObservation save(
            DataCollectionObservation observation) {

        Optional<DataCollectionObservationEntity> existing =
                jpaRepository.findById(
                        observation.getId().getValue());

        if (existing.isPresent()) {
            DataCollectionObservationEntity entity =
                    existing.get();

            entity.updateFrom(observation);

            return restoreDomain(entity);
        }

        DataCollectionObservationEntity entity =
                mapper.toEntity(observation);

        DataCollectionObservationEntity savedEntity =
                jpaRepository.save(entity);

        return restoreDomain(savedEntity);
    }

    @Override
    public Optional<DataCollectionObservation> findById(
            DataCollectionObservationId id) {

        return jpaRepository
                .findById(id.getValue())
                .map(this::restoreDomain);
    }

    @Override
    public List<DataCollectionObservation> findByDataCollectionId(
            DataCollectionId dataCollectionId) {

        return jpaRepository
                .findByDataCollectionIdOrderByReferenceYearAsc(
                        dataCollectionId.getValue())
                .stream()
                .map(this::restoreDomain)
                .toList();
    }

    @Override
    public List<DataCollectionObservation>
            findByQuestionnaireVariableId(
                    QuestionnaireVariableId questionnaireVariableId) {

        return jpaRepository
                .findByQuestionnaireVariableIdOrderByReferenceYearAsc(
                        questionnaireVariableId.getValue())
                .stream()
                .map(this::restoreDomain)
                .toList();
    }

    @Override
    public Optional<DataCollectionObservation>
            findByDataCollectionIdAndQuestionnaireVariableIdAndReferenceYear(
                    DataCollectionId dataCollectionId,
                    QuestionnaireVariableId questionnaireVariableId,
                    int referenceYear) {

        return jpaRepository
                .findByDataCollectionIdAndQuestionnaireVariableIdAndReferenceYear(
                        dataCollectionId.getValue(),
                        questionnaireVariableId.getValue(),
                        referenceYear)
                .map(this::restoreDomain);
    }

    @Override
    public boolean existsByDataCollectionIdAndQuestionnaireVariableIdAndReferenceYear(
            DataCollectionId dataCollectionId,
            QuestionnaireVariableId questionnaireVariableId,
            int referenceYear) {

        return jpaRepository
                .existsByDataCollectionIdAndQuestionnaireVariableIdAndReferenceYear(
                        dataCollectionId.getValue(),
                        questionnaireVariableId.getValue(),
                        referenceYear);
    }

    @Override
    public void delete(
            DataCollectionObservation observation) {

        jpaRepository.deleteById(
                observation.getId().getValue());
    }

    private DataCollectionObservation restoreDomain(
            DataCollectionObservationEntity entity) {

        QuestionnaireVariableId variableId =
                QuestionnaireVariableId.of(
                        entity.getQuestionnaireVariableId());

        QuestionnaireVariableDataType dataType =
                questionnaireVariableRepository
                        .findById(variableId)
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Questionnaire variable not found: "
                                                + entity.getQuestionnaireVariableId()))
                        .getDataType();

        return mapper.toDomain(
                entity,
                dataType);
    }
}
