package org.afdb.aikp.modules.questionnaire.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.afdb.aikp.modules.questionnaire.domain.enums.QuestionnaireStatus;
import org.afdb.aikp.modules.questionnaire.domain.model.Questionnaire;
import org.afdb.aikp.modules.questionnaire.domain.repository.QuestionnaireRepository;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireCode;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireId;
import org.afdb.aikp.modules.questionnaire.infrastructure.persistence.entity.QuestionnaireEntity;
import org.afdb.aikp.modules.questionnaire.infrastructure.persistence.mapper.QuestionnairePersistenceMapper;
import org.afdb.aikp.modules.questionnaire.infrastructure.persistence.repository.QuestionnaireJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Persistence adapter for Questionnaire aggregates.
 */
@Repository
public class QuestionnaireRepositoryAdapter
        implements QuestionnaireRepository {

    private final QuestionnaireJpaRepository jpaRepository;
    private final QuestionnairePersistenceMapper mapper;

    public QuestionnaireRepositoryAdapter(
            QuestionnaireJpaRepository jpaRepository,
            QuestionnairePersistenceMapper mapper) {

        this.jpaRepository = Objects.requireNonNull(
                jpaRepository,
                "QuestionnaireJpaRepository cannot be null.");

        this.mapper = Objects.requireNonNull(
                mapper,
                "QuestionnairePersistenceMapper cannot be null.");
    }

    @Override
    public Questionnaire save(
            Questionnaire questionnaire) {

        Objects.requireNonNull(
                questionnaire,
                "Questionnaire cannot be null.");

        UUID id = Objects.requireNonNull(
                questionnaire.getId().getValue(),
                "Questionnaire ID cannot be null.");

        QuestionnaireEntity entity =
                jpaRepository.findById(id)
                        .map(existingEntity -> {

                            mapper.updateEntity(
                                    questionnaire,
                                    existingEntity);

                            return existingEntity;
                        })
                        .orElseGet(() ->
                                mapper.toEntity(questionnaire));

        QuestionnaireEntity saved =
                jpaRepository.saveAndFlush(entity);

        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Questionnaire> findById(
            QuestionnaireId id) {

        Objects.requireNonNull(
                id,
                "QuestionnaireId cannot be null.");

        UUID value = Objects.requireNonNull(
                id.getValue(),
                "QuestionnaireId value cannot be null.");

        return jpaRepository.findById(value)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Questionnaire> findByCode(
            QuestionnaireCode code) {

        Objects.requireNonNull(
                code,
                "QuestionnaireCode cannot be null.");

        return jpaRepository
                .findByCode(code.getValue())
                .map(mapper::toDomain);
    }

    @Override
    public List<Questionnaire> findAll() {

        return jpaRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Questionnaire> findActive() {

        return jpaRepository.findByActiveTrue()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Questionnaire> findByStatus(
            QuestionnaireStatus status) {

        Objects.requireNonNull(
                status,
                "QuestionnaireStatus cannot be null.");

        return jpaRepository.findByStatus(status)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsById(
            QuestionnaireId id) {

        Objects.requireNonNull(
                id,
                "QuestionnaireId cannot be null.");

        return jpaRepository.existsById(
                id.getValue());
    }

    @Override
    public boolean existsByCode(
            QuestionnaireCode code) {

        Objects.requireNonNull(
                code,
                "QuestionnaireCode cannot be null.");

        return jpaRepository.existsByCode(
                code.getValue());
    }

    @Override
public void delete(
        Questionnaire questionnaire) {

    Objects.requireNonNull(
            questionnaire,
            "Questionnaire cannot be null.");

    UUID id = Objects.requireNonNull(
            questionnaire.getId().getValue(),
            "Questionnaire ID cannot be null.");

    jpaRepository.deleteById(id);

    jpaRepository.flush();
}
}