package org.afdb.aikp.modules.questionnaire.application.command;

import java.util.UUID;

/**
 * Command to reject a Questionnaire under review.
 */
public record RejectQuestionnaireCommand(
        UUID id
) {
}
