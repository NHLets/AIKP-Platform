package org.afdb.aikp.modules.campaigncountry.application.query;

import java.util.UUID;

/**
 * Query to retrieve a specific country association
 * within a campaign.
 */
public record GetCampaignCountryQuery(
        UUID campaignId,
        UUID countryId) {
}
