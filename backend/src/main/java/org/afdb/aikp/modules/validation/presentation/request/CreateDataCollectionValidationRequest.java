package org.afdb.aikp.modules.validation.presentation.request;

import java.time.Instant;
import java.util.UUID;
import org.afdb.aikp.modules.validation.domain.enums.ValidationDecision;

/** Request body for a decision; validator identity is taken from authentication. */
public record CreateDataCollectionValidationRequest(
        UUID dataCollectionId, ValidationDecision decision, String comments, Instant validatedAt) {
    /** Compatibility constructor: client-supplied validator identity is deliberately ignored. */
    @Deprecated
    public CreateDataCollectionValidationRequest(UUID dataCollectionId, UUID ignoredValidatorId,
            ValidationDecision decision, String comments, Instant validatedAt) {
        this(dataCollectionId, decision, comments, validatedAt);
    }
}
