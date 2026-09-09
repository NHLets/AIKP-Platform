package org.afdb.aikp.modules.questionnaire.domain.exception;

import org.afdb.aikp.shared.exception.ConflictException;

/**
 * Raised when a questionnaire variable series code
 * is already in use within a questionnaire.
 */
public class QuestionnaireVariableSeriesCodeAlreadyExistsException
        extends ConflictException {

    public QuestionnaireVariableSeriesCodeAlreadyExistsException(
            String seriesCode) {

        super(
                "A questionnaire variable with series code '"
                        + seriesCode
                        + "' already exists in this questionnaire."
        );
    }
}
