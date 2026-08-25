package org.afdb.aikp.modules.collection.application.command;

import java.util.UUID;

/**
 * Command to create a new DataCollection.
 */
public record CreateDataCollectionCommand(
        UUID campaignId,
        UUID countryId,
        UUID questionnaireId,
        UUID responsibleOrganizationId,
        UUID dataCollectorId) {
}
