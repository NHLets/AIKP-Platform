package org.afdb.aikp.modules.collection.infrastructure.persistence.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.afdb.aikp.modules.collection.domain.enums.DataCollectionStatus;

/**
 * JPA entity representing a DataCollection.
 */
@Entity
@Table(
        name = "data_collection",
        schema = "reference")
public class DataCollectionEntity {

    @Id
    @Column(
            name = "id",
            nullable = false,
            updatable = false)
    private UUID id;

    @Column(
            name = "campaign_id",
            nullable = false)
    private UUID campaignId;

    @Column(
            name = "country_id",
            nullable = false)
    private UUID countryId;

    @Column(
            name = "questionnaire_id",
            nullable = false)
    private UUID questionnaireId;

    @Column(
            name = "responsible_organization_id",
            nullable = false)
    private UUID responsibleOrganizationId;

    @Column(
            name = "data_collector_id",
            nullable = false)
    private UUID dataCollectorId;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 50)
    private DataCollectionStatus status;

    protected DataCollectionEntity() {
        // Required by JPA.
    }

    public DataCollectionEntity(
            UUID id,
            UUID campaignId,
            UUID countryId,
            UUID questionnaireId,
            UUID responsibleOrganizationId,
            UUID dataCollectorId,
            DataCollectionStatus status) {

        this.id = id;
        this.campaignId = campaignId;
        this.countryId = countryId;
        this.questionnaireId = questionnaireId;
        this.responsibleOrganizationId =
                responsibleOrganizationId;
        this.dataCollectorId = dataCollectorId;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public UUID getCampaignId() {
        return campaignId;
    }

    public UUID getCountryId() {
        return countryId;
    }

    public UUID getQuestionnaireId() {
        return questionnaireId;
    }

    public UUID getResponsibleOrganizationId() {
        return responsibleOrganizationId;
    }

    public UUID getDataCollectorId() {
        return dataCollectorId;
    }

    public DataCollectionStatus getStatus() {
        return status;
    }
}
