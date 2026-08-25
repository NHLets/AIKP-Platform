package org.afdb.aikp.modules.collection.application.command;

import java.util.UUID;

/**
 * Command to validate a submitted DataCollection.
 */
public record ValidateDataCollectionCommand(
        UUID dataCollectionId) {
}
