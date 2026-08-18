package org.afdb.aikp.modules.questionnaire.domain.exception;

import org.afdb.aikp.shared.exception.ConflictException;

/**
 * Exception thrown when an invalid Questionnaire lifecycle transition
 * is requested.
 */
public class QuestionnaireLifecycleException extends ConflictException {

    public QuestionnaireLifecycleException(String message) {
        super(message);
    }
}