package org.afdb.aikp.modules.campaigncountry.domain.model;

import java.time.Instant;
import java.util.Objects;

import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignId;
import org.afdb.aikp.modules.campaigncountry.domain.valueobject.CampaignCountryId;
import org.afdb.aikp.modules.country.domain.valueobject.CountryId;

/**
 * Represents the participation of a Country in a Campaign.
 */
public class CampaignCountry {

    private final CampaignCountryId id;

    private final CampaignId campaignId;

    private final CountryId countryId;

    private final Instant createdAt;

    private Instant updatedAt;

    private CampaignCountry(
            CampaignCountryId id,
            CampaignId campaignId,
            CountryId countryId,
            Instant createdAt,
            Instant updatedAt) {

        this.id = Objects.requireNonNull(
                id,
                "CampaignCountry ID cannot be null.");

        this.campaignId = Objects.requireNonNull(
                campaignId,
                "Campaign ID cannot be null.");

        this.countryId = Objects.requireNonNull(
                countryId,
                "Country ID cannot be null.");

        this.createdAt = Objects.requireNonNull(
                createdAt,
                "Created at cannot be null.");

        this.updatedAt = updatedAt;
    }

    public static CampaignCountry create(
            CampaignId campaignId,
            CountryId countryId) {

        Instant now = Instant.now();

        return new CampaignCountry(
                CampaignCountryId.generate(),
                campaignId,
                countryId,
                now,
                now);
    }

    public static CampaignCountry reconstitute(
            CampaignCountryId id,
            CampaignId campaignId,
            CountryId countryId,
            Instant createdAt,
            Instant updatedAt) {

        return new CampaignCountry(
                id,
                campaignId,
                countryId,
                createdAt,
                updatedAt);
    }

    public CampaignCountryId getId() {
        return id;
    }

    public CampaignId getCampaignId() {
        return campaignId;
    }

    public CountryId getCountryId() {
        return countryId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
