package org.afdb.aikp.modules.questionnaire.domain.exception;

/**
 * Exception thrown when a Questionnaire cannot be found.
 */
public class QuestionnaireNotFoundException extends RuntimeException {

    public QuestionnaireNotFoundException(String message) {
        super(message);
    }

}