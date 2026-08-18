package org.afdb.aikp.modules.questionnaire.domain.service;

import org.afdb.aikp.modules.questionnaire.domain.exception.QuestionnaireCodeAlreadyExistsException;
import org.afdb.aikp.modules.questionnaire.domain.repository.QuestionnaireRepository;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireCode;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireId;

import java.util.Objects;

/**
 * Default domain service implementation for the Questionnaire aggregate.
 */
public class QuestionnaireDomainServiceImpl
        implements QuestionnaireDomainService {

    private final QuestionnaireRepository repository;

    public QuestionnaireDomainServiceImpl(
            QuestionnaireRepository repository) {

        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public void validateCreation(QuestionnaireCode code) {

        Objects.requireNonNull(code);

        if (repository.existsByCode(code)) {
            throw new QuestionnaireCodeAlreadyExistsException(
                    code.getValue());
        }
    }

    @Override
    public void validateUpdate(
            QuestionnaireId questionnaireId,
            QuestionnaireCode code) {

        Objects.requireNonNull(questionnaireId);
        Objects.requireNonNull(code);

        repository.findByCode(code)
                .ifPresent(existing -> {

                    if (!existing.getId().equals(questionnaireId)) {
                        throw new QuestionnaireCodeAlreadyExistsException(
                                code.getValue());
                    }
                });
    }
}
