package org.afdb.aikp.modules.collection.application.query;

import java.util.UUID;

/**
 * Query to retrieve DataCollections for a campaign.
 */
public record GetDataCollectionsByCampaignQuery(
        UUID campaignId) {
}
