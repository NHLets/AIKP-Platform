package org.afdb.aikp.modules.questionnaire.application.command;

import java.util.UUID;

/**
 * Command to approve a Questionnaire.
 */
public record ApproveQuestionnaireCommand(
        UUID id
) {
}