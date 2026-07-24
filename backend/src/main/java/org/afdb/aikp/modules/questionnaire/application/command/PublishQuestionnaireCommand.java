package org.afdb.aikp.modules.questionnaire.application.command;

import java.util.UUID;

/**
 * Command to publish a Questionnaire.
 */
public record PublishQuestionnaireCommand(
        UUID id
) {
}