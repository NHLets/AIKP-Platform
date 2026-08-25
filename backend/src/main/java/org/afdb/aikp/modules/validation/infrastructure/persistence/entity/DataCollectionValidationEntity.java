package org.afdb.aikp.modules.validation.infrastructure.persistence.entity;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.afdb.aikp.modules.validation.domain.enums.ValidationDecision;

/**
 * JPA persistence entity for a data collection validation.
 */
@Entity
@Table(
        name = "data_collection_validation",
        schema = "reference")
public class DataCollectionValidationEntity {

    @Id
    @Column(
            name = "id",
            nullable = false,
            updatable = false)
    private UUID id;

    @Column(
            name = "data_collection_id",
            nullable = false,
            updatable = false)
    private UUID dataCollectionId;

    @Column(
            name = "validator_id",
            nullable = false,
            updatable = false)
    private UUID validatorId;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "decision",
            nullable = false,
            length = 30,
            updatable = false)
    private ValidationDecision decision;

    @Column(
            name = "comments",
            length = 4000,
            updatable = false)
    private String comments;

    @Column(
            name = "validated_at",
            nullable = false,
            updatable = false)
    private Instant validatedAt;

    /**
     * Required by JPA.
     */
    protected DataCollectionValidationEntity() {
        // Required by JPA.
    }

    public DataCollectionValidationEntity(
            UUID id,
            UUID dataCollectionId,
            UUID validatorId,
            ValidationDecision decision,
            String comments,
            Instant validatedAt) {

        this.id = id;
        this.dataCollectionId = dataCollectionId;
        this.validatorId = validatorId;
        this.decision = decision;
        this.comments = comments;
        this.validatedAt = validatedAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getDataCollectionId() {
        return dataCollectionId;
    }

    public UUID getValidatorId() {
        return validatorId;
    }

    public ValidationDecision getDecision() {
        return decision;
    }

    public String getComments() {
        return comments;
    }

    public Instant getValidatedAt() {
        return validatedAt;
    }
}
