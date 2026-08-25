package org.afdb.aikp.modules.campaigncountry.application.command;

import java.util.UUID;

/**
 * Command to remove a country from a campaign.
 */
public record RemoveCountryFromCampaignCommand(
        UUID campaignId,
        UUID countryId) {
}
