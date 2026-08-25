package org.afdb.aikp.modules.validation.application.query;

import java.util.UUID;

/**
 * Query for retrieving a data collection validation by its identifier.
 */
public record GetDataCollectionValidationQuery(
        UUID id) {
}
