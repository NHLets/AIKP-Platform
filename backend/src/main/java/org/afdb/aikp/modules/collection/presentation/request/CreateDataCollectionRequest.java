package org.afdb.aikp.modules.collection.presentation.request;

import java.util.UUID;

/**
 * REST request for creating a DataCollection.
 */
public record CreateDataCollectionRequest(
        UUID campaignId,
        UUID countryId,
        UUID questionnaireId,
        UUID responsibleOrganizationId,
        UUID dataCollectorId) {
}
