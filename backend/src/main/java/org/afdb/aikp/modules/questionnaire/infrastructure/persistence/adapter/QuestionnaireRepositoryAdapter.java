package org.afdb.aikp.modules.questionnaire.infrastructure.persistence.adapter;

import org.afdb.aikp.modules.questionnaire.domain.model.Questionnaire;
import org.afdb.aikp.modules.questionnaire.domain.repository.QuestionnaireRepository;
import org.afdb.aikp.modules.questionnaire.domain.enums.QuestionnaireStatus;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireCode;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireId;
import org.afdb.aikp.modules.questionnaire.infrastructure.persistence.entity.QuestionnaireEntity;
import org.afdb.aikp.modules.questionnaire.infrastructure.persistence.mapper.QuestionnairePersistenceMapper;
import org.afdb.aikp.modules.questionnaire.infrastructure.persistence.repository.QuestionnaireJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Objects;

@Repository
public class QuestionnaireRepositoryAdapter implements QuestionnaireRepository {

    private final QuestionnaireJpaRepository jpaRepository;
    private final QuestionnairePersistenceMapper mapper;

    public QuestionnaireRepositoryAdapter(
            QuestionnaireJpaRepository jpaRepository,
            QuestionnairePersistenceMapper mapper) {

        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Questionnaire save(Questionnaire questionnaire) {

        QuestionnaireEntity entity = mapper.toEntity(questionnaire);
        
        if (entity == null) {
            throw new IllegalStateException("Failed to map Questionnaire to entity.");
}
        QuestionnaireEntity saved = jpaRepository.save(entity);

        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Questionnaire> findById(QuestionnaireId id) {

    Objects.requireNonNull(id, "QuestionnaireId cannot be null.");

    return jpaRepository.findById(
            Objects.requireNonNull(id.getValue(), "QuestionnaireId value cannot be null."))
            .map(mapper::toDomain);
    }

    @Override
    public Optional<Questionnaire> findByCode(QuestionnaireCode code) {

        return jpaRepository.findByCode(code.getValue())
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

        return jpaRepository.findByStatus(status)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsById(QuestionnaireId id) {

        return jpaRepository.existsById(id.getValue());
    }

    @Override
    public boolean existsByCode(QuestionnaireCode code) {

        return jpaRepository.existsByCode(code.getValue());
    }

    @Override
public void delete(Questionnaire questionnaire) {

    jpaRepository.findById(questionnaire.getId().getValue())
            .ifPresent(jpaRepository::delete);
}
}