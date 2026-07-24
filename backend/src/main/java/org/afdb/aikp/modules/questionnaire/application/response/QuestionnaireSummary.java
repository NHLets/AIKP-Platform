package org.afdb.aikp.modules.questionnaire.application.response;

import java.util.UUID;

/**
 * Summary response representing a Questionnaire.
 */
public record QuestionnaireSummary(

        UUID id,

        String code,

        String name,

        String version,

        String status,

        boolean active

) {
}