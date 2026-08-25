package org.afdb.aikp.modules.validation.domain.model;

import java.time.Instant;
import java.util.Objects;

import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionId;
import org.afdb.aikp.modules.person.domain.valueobject.PersonId;
import org.afdb.aikp.modules.validation.domain.enums.ValidationDecision;
import org.afdb.aikp.modules.validation.domain.valueobject.DataCollectionValidationId;
import org.afdb.aikp.modules.validation.domain.valueobject.ValidationComments;
import org.afdb.aikp.shared.domain.AggregateRoot;

/**
 * DataCollectionValidation Aggregate Root.
 *
 * <p>
 * Represents an official validation decision made for a submitted
 * data collection.
 * </p>
 */
public final class DataCollectionValidation
        extends AggregateRoot<DataCollectionValidationId> {

    private final DataCollectionId dataCollectionId;

    private final PersonId validatorId;

    private final ValidationDecision decision;

    private final ValidationComments comments;

    private final Instant validatedAt;

    private DataCollectionValidation(
            DataCollectionValidationId id,
            DataCollectionId dataCollectionId,
            PersonId validatorId,
            ValidationDecision decision,
            ValidationComments comments,
            Instant validatedAt) {

        super(Objects.requireNonNull(
                id,
                "Data collection validation ID cannot be null."));

        this.dataCollectionId = Objects.requireNonNull(
                dataCollectionId,
                "Data collection ID cannot be null.");

        this.validatorId = Objects.requireNonNull(
                validatorId,
                "Validator ID cannot be null.");

        this.decision = Objects.requireNonNull(
                decision,
                "Validation decision cannot be null.");

        this.comments = comments;

        this.validatedAt = Objects.requireNonNull(
                validatedAt,
                "Validation timestamp cannot be null.");
    }

    /**
     * Creates a new validation decision.
     */
    public static DataCollectionValidation create(
            DataCollectionValidationId id,
            DataCollectionId dataCollectionId,
            PersonId validatorId,
            ValidationDecision decision,
            ValidationComments comments,
            Instant validatedAt) {

        return new DataCollectionValidation(
                id,
                dataCollectionId,
                validatorId,
                decision,
                comments,
                validatedAt);
    }

    /**
     * Restores an existing validation decision from persistence.
     */
    public static DataCollectionValidation restore(
            DataCollectionValidationId id,
            DataCollectionId dataCollectionId,
            PersonId validatorId,
            ValidationDecision decision,
            ValidationComments comments,
            Instant validatedAt) {

        return new DataCollectionValidation(
                id,
                dataCollectionId,
                validatorId,
                decision,
                comments,
                validatedAt);
    }

    public DataCollectionValidationId
            getDataCollectionValidationId() {

        return getId();
    }

    public DataCollectionId getDataCollectionId() {
        return dataCollectionId;
    }

    public PersonId getValidatorId() {
        return validatorId;
    }

    public ValidationDecision getDecision() {
        return decision;
    }

    public ValidationComments getComments() {
        return comments;
    }

    public Instant getValidatedAt() {
        return validatedAt;
    }
}
