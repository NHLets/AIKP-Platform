package org.afdb.aikp.modules.validation.domain.model;

import org.afdb.aikp.modules.validation.domain.enums.ValidationSeverity;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class ValidationComment {

    private final UUID id;
    private final UUID observationId;
    private final UUID validatorId;
    private final String comment;
    private final ValidationSeverity severity;
    private final Instant createdAt;
    private final Instant updatedAt;

    public ValidationComment(
            UUID id,
            UUID observationId,
            UUID validatorId,
            String comment,
            ValidationSeverity severity,
            Instant createdAt,
            Instant updatedAt) {

        this.id = Objects.requireNonNull(id);
        this.observationId = Objects.requireNonNull(observationId);
        this.validatorId = Objects.requireNonNull(validatorId);
        this.comment = Objects.requireNonNull(comment).trim();
        this.severity = Objects.requireNonNull(severity);
        this.createdAt = Objects.requireNonNull(createdAt);
        this.updatedAt = Objects.requireNonNull(updatedAt);

        if (this.comment.isBlank()) {
            throw new IllegalArgumentException(
                    "Validation comment cannot be blank.");
        }
    }

    public static ValidationComment create(
            UUID observationId,
            UUID validatorId,
            String comment,
            ValidationSeverity severity) {

        Instant now = Instant.now();

        return new ValidationComment(
                UUID.randomUUID(),
                observationId,
                validatorId,
                comment,
                severity,
                now,
                now);
    }

    public UUID getId() {
        return id;
    }

    public UUID getObservationId() {
        return observationId;
    }

    public UUID getValidatorId() {
        return validatorId;
    }

    public String getComment() {
        return comment;
    }

    public ValidationSeverity getSeverity() {
        return severity;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public ValidationComment updateComment(
            String newComment,
            ValidationSeverity newSeverity) {

        return new ValidationComment(
                id,
                observationId,
                validatorId,
                newComment,
                newSeverity,
                createdAt,
                Instant.now());
    }
}
