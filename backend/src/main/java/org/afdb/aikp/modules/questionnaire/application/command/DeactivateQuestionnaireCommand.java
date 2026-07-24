package org.afdb.aikp.modules.questionnaire.application.command;

import java.util.UUID;

/**
 * Command to deactivate a Questionnaire.
 */
public record DeactivateQuestionnaireCommand(
        UUID id
) {
}