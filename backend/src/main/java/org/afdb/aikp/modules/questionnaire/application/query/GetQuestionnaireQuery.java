package org.afdb.aikp.modules.questionnaire.application.query;

import java.util.UUID;

/**
 * Query to retrieve a Questionnaire by its identifier.
 */
public record GetQuestionnaireQuery(

        UUID id

) {
}