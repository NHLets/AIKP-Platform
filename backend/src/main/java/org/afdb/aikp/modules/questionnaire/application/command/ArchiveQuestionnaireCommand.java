package org.afdb.aikp.modules.questionnaire.application.command;

import java.util.UUID;

/**
 * Command to activate a Questionnaire.
 */
public record ArchiveQuestionnaireCommand(
        UUID id
) {
}