package org.afdb.aikp.modules.collection.application.command;

import java.util.UUID;

/**
 * Command to update the responsible organization
 * and designated data collector of a DataCollection.
 */
public record UpdateDataCollectionCommand(
        UUID dataCollectionId,
        UUID responsibleOrganizationId,
        UUID dataCollectorId) {
}
