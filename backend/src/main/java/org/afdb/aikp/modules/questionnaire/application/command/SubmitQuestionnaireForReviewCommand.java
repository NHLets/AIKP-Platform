package org.afdb.aikp.modules.questionnaire.application.command;

import java.util.UUID;

/**
 * Command to submit a Questionnaire for review.
 */
public record SubmitQuestionnaireForReviewCommand(
        UUID id
) {
}