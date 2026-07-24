package org.afdb.aikp.modules.questionnaire.application.command;

import org.afdb.aikp.modules.questionnaire.domain.enums.RenderType;

import java.util.UUID;

/**
 * Command to update a Questionnaire.
 */
public record UpdateQuestionnaireCommand(

        UUID id,

        String name,

        String description,

        String version,

        String defaultLanguage,

        RenderType renderType

) {
}