package org.afdb.aikp.modules.questionnaire.application.command;

import java.util.UUID;

/**
 * Command to archive a Questionnaire.
 */
public record ActivateQuestionnaireCommand(
        UUID id
) {
}