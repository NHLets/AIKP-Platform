package org.afdb.aikp.modules.collection.application.query;

import java.util.UUID;

/**
 * Query to retrieve a DataCollection by identifier.
 */
public record GetDataCollectionQuery(
        UUID dataCollectionId) {
}
