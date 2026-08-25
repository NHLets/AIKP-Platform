package org.afdb.aikp.modules.collection.application.command;

import java.util.UUID;

/**
 * Command to delete a DataCollection.
 */
public record DeleteDataCollectionCommand(
        UUID dataCollectionId) {
}
