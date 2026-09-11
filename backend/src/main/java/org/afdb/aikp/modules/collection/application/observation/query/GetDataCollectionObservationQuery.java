package org.afdb.aikp.modules.collection.application.observation.query;

import java.util.UUID;

/**
 * Query for retrieving one data collection observation.
 */
public record GetDataCollectionObservationQuery(
        UUID dataCollectionObservationId) {
}
