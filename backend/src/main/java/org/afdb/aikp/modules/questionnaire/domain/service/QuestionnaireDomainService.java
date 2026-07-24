package org.afdb.aikp.modules.questionnaire.domain.service;

import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireCode;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireId;

/**
 * Domain service for Questionnaire aggregate.
 */
public interface QuestionnaireDomainService {

    /**
     * Validates that a questionnaire can be created.
     */
    void validateCreation(
            QuestionnaireCode code);

    /**
     * Validates that a questionnaire can be updated.
     */
    void validateUpdate(
            QuestionnaireId questionnaireId,
            QuestionnaireCode code);

}