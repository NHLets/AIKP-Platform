package org.afdb.aikp.modules.questionnaire.presentation.mapper;

import org.afdb.aikp.modules.questionnaire.application.command.CreateQuestionnaireCommand;
import org.afdb.aikp.modules.questionnaire.application.command.UpdateQuestionnaireCommand;
import org.afdb.aikp.modules.questionnaire.presentation.request.CreateQuestionnaireRequest;
import org.afdb.aikp.modules.questionnaire.presentation.request.UpdateQuestionnaireRequest;

import java.util.UUID;

/**
 * Maps REST requests to Questionnaire application commands.
 */
public final class QuestionnaireRestMapper {

    private QuestionnaireRestMapper() {
        // Utility class
    }

    /**
     * Maps a create request to a create command.
     */
    public static CreateQuestionnaireCommand toCommand(
            CreateQuestionnaireRequest request) {

        return new CreateQuestionnaireCommand(
                request.code(),
                request.name(),
                request.description(),
                request.version(),
                request.defaultLanguage(),
                request.renderType()
        );
    }

    /**
     * Maps an update request to an update command.
     */
    public static UpdateQuestionnaireCommand toCommand(
            UUID id,
            UpdateQuestionnaireRequest request) {

        return new UpdateQuestionnaireCommand(
                id,
                request.name(),
                request.description(),
                request.version(),
                request.defaultLanguage(),
                request.renderType()
        );
    }

}