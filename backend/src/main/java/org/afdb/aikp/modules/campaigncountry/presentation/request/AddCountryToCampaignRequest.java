package org.afdb.aikp.modules.campaigncountry.presentation.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

/**
 * REST request for adding a country
 * to a campaign.
 */
public record AddCountryToCampaignRequest(

        @NotNull(message = "Country ID is required.")
        UUID countryId
) {
}
