package org.afdb.aikp.modules.collection.application.command;

import java.util.UUID;

/**
 * Command to reject a submitted DataCollection.
 */
public record RejectDataCollectionCommand(
        UUID dataCollectionId) {
}
