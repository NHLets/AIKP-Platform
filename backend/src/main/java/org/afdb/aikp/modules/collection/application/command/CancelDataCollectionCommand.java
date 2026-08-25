package org.afdb.aikp.modules.collection.application.command;

import java.util.UUID;

/**
 * Command to cancel a DataCollection.
 */
public record CancelDataCollectionCommand(
        UUID dataCollectionId) {
}
