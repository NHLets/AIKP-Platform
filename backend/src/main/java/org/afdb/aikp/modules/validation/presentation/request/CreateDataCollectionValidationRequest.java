package org.afdb.aikp.modules.validation.presentation.request;

import java.time.Instant;
import java.util.UUID;

import org.afdb.aikp.modules.validation.domain.enums.ValidationDecision;

/**
 * REST request for creating a data collection validation.
 */
public record CreateDataCollectionValidationRequest(
        UUID dataCollectionId,
        UUID validatorId,
        ValidationDecision decision,
        String comments,
        Instant validatedAt) {
}
