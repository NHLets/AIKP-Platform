package org.afdb.aikp.modules.dataCollection.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import org.afdb.aikp.modules.dataCollection.domain.model.ObservationStatus;

@Entity
@Table(name = "data_collection_observation")
public class ObservationEntity {

    @Id
    private UUID id;

    @Column(name = "data_collection_id", nullable = false)
    private UUID dataCollectionId;

    @Column(name = "variable_id", nullable = false)
    private UUID variableId;

    @Column(name = "organization_id", nullable = false)
    private UUID organizationId;

    @Column(columnDefinition = "TEXT")
    private String value;

    @Enumerated(EnumType.STRING)
    private ObservationStatus status;

    @Column(name = "updated_at")
    private Instant updatedAt;

    public ObservationEntity() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getDataCollectionId() { return dataCollectionId; }
    public void setDataCollectionId(UUID dataCollectionId) { this.dataCollectionId = dataCollectionId; }

    public UUID getVariableId() { return variableId; }
    public void setVariableId(UUID variableId) { this.variableId = variableId; }

    public UUID getOrganizationId() { return organizationId; }
    public void setOrganizationId(UUID organizationId) { this.organizationId = organizationId; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public ObservationStatus getStatus() { return status; }
    public void setStatus(ObservationStatus status) { this.status = status; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
