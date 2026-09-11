package org.afdb.aikp.modules.collection.application.observation.query;

import java.util.UUID;

/**
 * Query for retrieving observations belonging to a data collection.
 */
public record GetDataCollectionObservationsQuery(
        UUID dataCollectionId) {
}
