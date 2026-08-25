package org.afdb.aikp.modules.validation.application.query;

import java.util.UUID;

/**
 * Query for retrieving all validations of a data collection.
 */
public record GetDataCollectionValidationsByDataCollectionQuery(
        UUID dataCollectionId) {
}
