package org.afdb.aikp.modules.campaigncountry.domain.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.UUID;

import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignId;
import org.afdb.aikp.modules.campaigncountry.domain.valueobject.CampaignCountryId;
import org.afdb.aikp.modules.country.domain.valueobject.CountryId;

import org.junit.jupiter.api.Test;

class CampaignCountryTest {

    @Test
    void shouldCreateCampaignCountry() {

        CampaignId campaignId =
                CampaignId.of(
                        UUID.randomUUID());

        CountryId countryId =
                CountryId.of(
                        UUID.randomUUID());

        CampaignCountry campaignCountry =
                CampaignCountry.create(
                        campaignId,
                        countryId);

        assertThat(campaignCountry)
                .isNotNull();

        assertThat(campaignCountry.getId())
                .isNotNull();

        assertThat(campaignCountry.getCampaignId())
                .isEqualTo(campaignId);

        assertThat(campaignCountry.getCountryId())
                .isEqualTo(countryId);

        assertThat(campaignCountry.getCreatedAt())
                .isNotNull();

        assertThat(campaignCountry.getUpdatedAt())
                .isNotNull();

        assertThat(campaignCountry.getUpdatedAt())
                .isEqualTo(
                        campaignCountry.getCreatedAt());
    }

    @Test
    void shouldReconstituteCampaignCountry() {

        CampaignCountryId id =
                CampaignCountryId.of(
                        UUID.randomUUID());

        CampaignId campaignId =
                CampaignId.of(
                        UUID.randomUUID());

        CountryId countryId =
                CountryId.of(
                        UUID.randomUUID());

        Instant createdAt =
                Instant.parse(
                        "2025-01-01T10:00:00Z");

        Instant updatedAt =
                Instant.parse(
                        "2025-01-02T10:00:00Z");

        CampaignCountry campaignCountry =
                CampaignCountry.reconstitute(
                        id,
                        campaignId,
                        countryId,
                        createdAt,
                        updatedAt);

        assertThat(campaignCountry.getId())
                .isEqualTo(id);

        assertThat(campaignCountry.getCampaignId())
                .isEqualTo(campaignId);

        assertThat(campaignCountry.getCountryId())
                .isEqualTo(countryId);

        assertThat(campaignCountry.getCreatedAt())
                .isEqualTo(createdAt);

        assertThat(campaignCountry.getUpdatedAt())
                .isEqualTo(updatedAt);
    }
}
