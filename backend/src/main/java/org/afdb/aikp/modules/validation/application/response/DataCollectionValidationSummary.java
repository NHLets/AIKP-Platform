package org.afdb.aikp.modules.validation.application.response;

import java.time.Instant;
import java.util.UUID;

import org.afdb.aikp.modules.validation.domain.enums.ValidationDecision;

/**
 * Summary representation of a data collection validation.
 */
public record DataCollectionValidationSummary(
        UUID id,
        UUID validatorId,
        ValidationDecision decision,
        Instant validatedAt) {
}
