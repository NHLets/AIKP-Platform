package org.afdb.aikp.modules.collection.application.command;

import java.util.UUID;

/**
 * Command to submit a DataCollection for validation.
 */
public record SubmitDataCollectionCommand(
        UUID dataCollectionId) {
}
