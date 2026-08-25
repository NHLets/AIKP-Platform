package org.afdb.aikp.modules.campaigncountry.application.response;

import java.time.Instant;
import java.util.UUID;

/**
 * Application response representing a country's
 * participation in a campaign.
 */
public record CampaignCountryResponse(
        UUID id,
        UUID campaignId,
        UUID countryId,
        Instant createdAt,
        Instant updatedAt) {
}
