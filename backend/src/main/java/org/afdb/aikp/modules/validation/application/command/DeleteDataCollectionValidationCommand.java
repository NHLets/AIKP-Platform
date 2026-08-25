package org.afdb.aikp.modules.validation.application.command;

import java.util.UUID;

/**
 * Command for deleting a data collection validation.
 */
public record DeleteDataCollectionValidationCommand(
        UUID id) {
}
