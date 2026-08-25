package org.afdb.aikp.modules.collection.presentation.request;

import java.util.UUID;

/**
 * REST request for updating the responsible organization
 * and data collector of a DataCollection.
 */
public record UpdateDataCollectionRequest(
        UUID responsibleOrganizationId,
        UUID dataCollectorId) {
}
