package org.afdb.aikp.modules.validation.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.afdb.aikp.modules.validation.domain.enums.ValidationSeverity;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        schema = "reference",
        name = "validation_comment",
        indexes = {
                @Index(
                        name = "idx_validation_comment_observation",
                        columnList = "observation_id"),
                @Index(
                        name = "idx_validation_comment_validator",
                        columnList = "validator_id")
        })
public class ValidationCommentEntity {

    @Id
    private UUID id;

    @Column(name = "observation_id", nullable = false)
    private UUID observationId;

    @Column(name = "validator_id", nullable = false)
    private UUID validatorId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String comment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ValidationSeverity severity;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public ValidationCommentEntity() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getObservationId() {
        return observationId;
    }

    public void setObservationId(UUID observationId) {
        this.observationId = observationId;
    }

    public UUID getValidatorId() {
        return validatorId;
    }

    public void setValidatorId(UUID validatorId) {
        this.validatorId = validatorId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ValidationSeverity getSeverity() {
        return severity;
    }

    public void setSeverity(ValidationSeverity severity) {
        this.severity = severity;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
