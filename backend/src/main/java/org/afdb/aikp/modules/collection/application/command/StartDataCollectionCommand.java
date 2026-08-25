package org.afdb.aikp.modules.collection.application.command;

import java.util.UUID;

/**
 * Command to start a DataCollection.
 */
public record StartDataCollectionCommand(
        UUID dataCollectionId) {
}
