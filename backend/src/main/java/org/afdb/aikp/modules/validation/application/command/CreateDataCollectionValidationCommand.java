package org.afdb.aikp.modules.validation.application.command;

import java.time.Instant;
import java.util.UUID;

import org.afdb.aikp.modules.validation.domain.enums.ValidationDecision;

/**
 * Command for creating a data collection validation.
 */
public record CreateDataCollectionValidationCommand(
        UUID dataCollectionId,
        UUID validatorId,
        ValidationDecision decision,
        String comments,
        Instant validatedAt) {
}
