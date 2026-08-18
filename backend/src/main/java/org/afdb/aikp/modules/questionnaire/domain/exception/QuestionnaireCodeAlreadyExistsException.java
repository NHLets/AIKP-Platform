package org.afdb.aikp.modules.questionnaire.domain.exception;

/**
 * Exception thrown when a questionnaire code is already in use.
 */
public class QuestionnaireCodeAlreadyExistsException extends RuntimeException {

    public QuestionnaireCodeAlreadyExistsException(String code) {
        super("Questionnaire code already exists: " + code);
    }
}
