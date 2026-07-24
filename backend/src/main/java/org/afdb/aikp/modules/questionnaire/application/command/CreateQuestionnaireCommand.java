package org.afdb.aikp.modules.questionnaire.application.command;

import org.afdb.aikp.modules.questionnaire.domain.enums.RenderType;

/**
 * Command to create a Questionnaire.
 */
public record CreateQuestionnaireCommand(

        String code,

        String name,

        String description,

        String version,

        String defaultLanguage,

        RenderType renderType

) {
}