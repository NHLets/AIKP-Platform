package org.afdb.aikp.modules.validation.application.response;

import java.time.Instant;
import java.util.UUID;

import org.afdb.aikp.modules.validation.domain.enums.ValidationDecision;

/**
 * Complete application response for a data collection validation.
 */
public record DataCollectionValidationResponse(
        UUID id,
        UUID dataCollectionId,
        UUID validatorId,
        ValidationDecision decision,
        String comments,
        Instant validatedAt) {
}
