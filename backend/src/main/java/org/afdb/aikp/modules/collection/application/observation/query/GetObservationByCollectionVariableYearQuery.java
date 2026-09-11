package org.afdb.aikp.modules.collection.application.observation.query;

import java.util.UUID;

/**
 * Query for retrieving an observation by collection,
 * questionnaire variable and reference year.
 */
public record GetObservationByCollectionVariableYearQuery(
        UUID dataCollectionId,
        UUID questionnaireVariableId,
        int referenceYear) {
}
