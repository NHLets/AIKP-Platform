package org.afdb.aikp.modules.dataCollection.domain.model;

import java.time.Instant;
import java.util.UUID;

public class Observation {

    private final ObservationId id;
    private final UUID dataCollectionId;
    private final UUID variableId;
    private final UUID organizationId;

    private String value;
    private ObservationStatus status;
    private Instant updatedAt;

    public Observation(
            ObservationId id,
            UUID dataCollectionId,
            UUID variableId,
            UUID organizationId,
            String value
    ) {
        this.id = id;
        this.dataCollectionId = dataCollectionId;
        this.variableId = variableId;
        this.organizationId = organizationId;
        this.value = value;
        this.status = ObservationStatus.DRAFT;
        this.updatedAt = Instant.now();
    }

    public ObservationId getId() {
        return id;
    }

    public UUID getDataCollectionId() {
        return dataCollectionId;
    }

    public UUID getVariableId() {
        return variableId;
    }

    public UUID getOrganizationId() {
        return organizationId;
    }

    public String getValue() {
        return value;
    }

    public ObservationStatus getStatus() {
        return status;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void updateValue(String value) {
        this.value = value;
        this.updatedAt = Instant.now();
    }

    public void submit() {
        this.status = ObservationStatus.SUBMITTED;
        this.updatedAt = Instant.now();
    }

    public void validate() {
        this.status = ObservationStatus.VALIDATED;
        this.updatedAt = Instant.now();
    }
}
