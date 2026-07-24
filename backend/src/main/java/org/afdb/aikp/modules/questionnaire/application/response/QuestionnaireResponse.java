package org.afdb.aikp.modules.questionnaire.application.response;

import java.util.UUID;

/**
 * Detailed response representing a Questionnaire.
 */
public record QuestionnaireResponse(

        UUID id,

        String code,

        String name,

        String description,

        String version,

        String defaultLanguage,

        String status,

        String renderType,

        boolean active

) {
}
