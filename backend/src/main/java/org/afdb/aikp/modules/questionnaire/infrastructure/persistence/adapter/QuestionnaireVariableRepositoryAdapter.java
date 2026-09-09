package org.afdb.aikp.modules.questionnaire.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import org.afdb.aikp.modules.questionnaire.domain.model.QuestionnaireVariable;
import org.afdb.aikp.modules.questionnaire.domain.repository.QuestionnaireVariableRepository;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireGroupId;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireId;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireVariableId;
import org.afdb.aikp.modules.questionnaire.infrastructure.persistence.entity.QuestionnaireVariableEntity;
import org.afdb.aikp.modules.questionnaire.infrastructure.persistence.mapper.QuestionnaireVariablePersistenceMapper;
import org.afdb.aikp.modules.questionnaire.infrastructure.persistence.repository.QuestionnaireVariableJpaRepository;

/**
 * Persistence adapter for QuestionnaireVariable aggregates.
 */
@Repository
public class QuestionnaireVariableRepositoryAdapter
        implements QuestionnaireVariableRepository {

    private final QuestionnaireVariableJpaRepository jpaRepository;

    private final QuestionnaireVariablePersistenceMapper mapper;

    private final EntityManager entityManager;

    public QuestionnaireVariableRepositoryAdapter(
            QuestionnaireVariableJpaRepository jpaRepository,
            QuestionnaireVariablePersistenceMapper mapper,
            EntityManager entityManager) {

        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
        this.entityManager = entityManager;
    }

    @Override
    public QuestionnaireVariable save(
            QuestionnaireVariable variable) {

        QuestionnaireVariableEntity entity =
                jpaRepository.findById(
                                variable.getId().getValue())
                        .orElseGet(() ->
                                mapper.toEntity(variable));

        mapper.updateEntity(variable, entity);

        QuestionnaireVariableEntity saved =
                jpaRepository.saveAndFlush(entity);

        return mapper.toDomain(saved);
    }

    @Override
    public Optional<QuestionnaireVariable> findById(
            QuestionnaireVariableId id) {

        return jpaRepository
                .findById(id.getValue())
                .map(mapper::toDomain);
    }

    @Override
    public List<QuestionnaireVariable> findByQuestionnaireId(
            QuestionnaireId questionnaireId) {

        return jpaRepository
                .findByQuestionnaireIdOrderByDisplayOrderAsc(
                        questionnaireId.getValue())
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<QuestionnaireVariable> findByQuestionnaireGroupId(
            QuestionnaireGroupId questionnaireGroupId) {

        return jpaRepository
                .findByQuestionnaireGroupIdOrderByDisplayOrderAsc(
                        questionnaireGroupId.getValue())
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByQuestionnaireIdAndSeriesCode(
            QuestionnaireId questionnaireId,
            String seriesCode) {

        return jpaRepository
                .existsByQuestionnaireIdAndSeriesCode(
                        questionnaireId.getValue(),
                        seriesCode);
    }

    @Override
    public void delete(
            QuestionnaireVariable variable) {

        QuestionnaireVariableEntity entity =
                jpaRepository.findById(
                        variable.getId().getValue())
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Questionnaire variable not found with id: "
                                                + variable.getId().getValue()));

        jpaRepository.delete(entity);
        jpaRepository.flush();
        entityManager.clear();
    }
}
