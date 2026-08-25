package org.afdb.aikp.modules.campaigncountry.application.command;

import java.util.UUID;

/**
 * Command to associate a country with a campaign.
 */
public record AddCountryToCampaignCommand(
        UUID campaignId,
        UUID countryId) {
}
