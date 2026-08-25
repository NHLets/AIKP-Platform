package org.afdb.aikp.modules.campaigncountry.infrastructure.persistence.entity;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import org.afdb.aikp.shared.persistence.BaseEntity;

/**
 * JPA entity representing the association between
 * a Campaign and a Country.
 */
@Entity
@Table(
        name = "campaign_country",
        schema = "campaign"
)
public class CampaignCountryEntity extends BaseEntity {

    @Column(
            name = "campaign_id",
            nullable = false,
            updatable = false
    )
    private UUID campaignId;

    @Column(
            name = "country_id",
            nullable = false,
            updatable = false
    )
    private UUID countryId;

    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private Instant createdAt;

    @Column(
            name = "updated_at"
    )
    private Instant updatedAt;

    /**
     * Required by JPA.
     */
    protected CampaignCountryEntity() {
        super();
    }

    /**
     * Creates a CampaignCountry persistence entity.
     *
     * @param id association identifier
     * @param campaignId campaign identifier
     * @param countryId country identifier
     * @param createdAt creation timestamp
     * @param updatedAt last update timestamp
     */
    public CampaignCountryEntity(
            UUID id,
            UUID campaignId,
            UUID countryId,
            Instant createdAt,
            Instant updatedAt) {

        super(id);

        this.campaignId = campaignId;
        this.countryId = countryId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getCampaignId() {
        return campaignId;
    }

    public UUID getCountryId() {
        return countryId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}