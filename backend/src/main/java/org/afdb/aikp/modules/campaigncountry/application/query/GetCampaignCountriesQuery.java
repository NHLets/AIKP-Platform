package org.afdb.aikp.modules.campaigncountry.application.query;

import java.util.UUID;

/**
 * Query to retrieve countries associated with a campaign.
 */
public record GetCampaignCountriesQuery(
        UUID campaignId) {
}
