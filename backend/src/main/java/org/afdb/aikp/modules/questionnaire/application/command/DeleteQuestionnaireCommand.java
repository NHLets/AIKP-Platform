package org.afdb.aikp.modules.questionnaire.application.command;

import java.util.UUID;

/**
 * Command to delete a Questionnaire.
 */
public record DeleteQuestionnaireCommand(
        UUID id
) {
}