package org.afdb.aikp.modules.collection.application.observation.command;

import java.util.UUID;

/**
 * Command for deleting a data collection observation.
 */
public record DeleteDataCollectionObservationCommand(
        UUID dataCollectionObservationId) {
}
