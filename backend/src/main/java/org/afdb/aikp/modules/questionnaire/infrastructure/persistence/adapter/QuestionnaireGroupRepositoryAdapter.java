package org.afdb.aikp.modules.questionnaire.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import org.afdb.aikp.modules.questionnaire.domain.model.QuestionnaireGroup;
import org.afdb.aikp.modules.questionnaire.domain.repository.QuestionnaireGroupRepository;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireGroupId;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireId;
import org.afdb.aikp.modules.questionnaire.infrastructure.persistence.entity.QuestionnaireGroupEntity;
import org.afdb.aikp.modules.questionnaire.infrastructure.persistence.mapper.QuestionnaireGroupPersistenceMapper;
import org.afdb.aikp.modules.questionnaire.infrastructure.persistence.repository.QuestionnaireGroupJpaRepository;

@Repository
public class QuestionnaireGroupRepositoryAdapter
        implements QuestionnaireGroupRepository {

    private final QuestionnaireGroupJpaRepository jpaRepository;

    private final QuestionnaireGroupPersistenceMapper mapper;

    private final EntityManager entityManager;

    public QuestionnaireGroupRepositoryAdapter(
            QuestionnaireGroupJpaRepository jpaRepository,
            QuestionnaireGroupPersistenceMapper mapper,
            EntityManager entityManager) {

        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
        this.entityManager = entityManager;
    }

    @Override
    public QuestionnaireGroup save(
            QuestionnaireGroup group) {

        QuestionnaireGroupEntity entity =
                jpaRepository.findById(
                                group.getId().getValue())
                        .orElseGet(() ->
                                mapper.toEntity(group));

        mapper.updateEntity(group, entity);

        QuestionnaireGroupEntity saved =
                jpaRepository.saveAndFlush(entity);

        return mapper.toDomain(saved);
    }

    @Override
    public Optional<QuestionnaireGroup> findById(
            QuestionnaireGroupId id) {

        return jpaRepository
                .findById(id.getValue())
                .map(mapper::toDomain);
    }

    @Override
    public List<QuestionnaireGroup> findByQuestionnaireId(
            QuestionnaireId questionnaireId) {

        return jpaRepository
                .findByQuestionnaireIdOrderByDisplayOrderAsc(
                        questionnaireId.getValue())
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<QuestionnaireGroup>
            findRootGroupsByQuestionnaireId(
                    QuestionnaireId questionnaireId) {

        return jpaRepository
                .findByQuestionnaireIdAndParentGroupIdIsNullOrderByDisplayOrderAsc(
                        questionnaireId.getValue())
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<QuestionnaireGroup>
            findByQuestionnaireIdAndParentGroupId(
                    QuestionnaireId questionnaireId,
                    QuestionnaireGroupId parentGroupId) {

        return jpaRepository
                .findByQuestionnaireIdAndParentGroupIdOrderByDisplayOrderAsc(
                        questionnaireId.getValue(),
                        parentGroupId.getValue())
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsRootGroupCode(
            QuestionnaireId questionnaireId,
            String code) {

        return jpaRepository
                .existsByQuestionnaireIdAndCodeAndParentGroupIdIsNull(
                        questionnaireId.getValue(),
                        code);
    }

    @Override
    public boolean existsChildGroupCode(
            QuestionnaireId questionnaireId,
            QuestionnaireGroupId parentGroupId,
            String code) {

        return jpaRepository
                .existsByQuestionnaireIdAndParentGroupIdAndCode(
                        questionnaireId.getValue(),
                        parentGroupId.getValue(),
                        code);
    }

    @Override
    public void delete(
            QuestionnaireGroup group) {

        QuestionnaireGroupEntity entity =
                jpaRepository.findById(
                        group.getId().getValue())
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Questionnaire group not found with id: "
                                                + group.getId().getValue()));

        jpaRepository.delete(entity);
        jpaRepository.flush();
        entityManager.clear();
    }
}
