package org.afdb.aikp.modules.campaign.application.mapper;

import org.afdb.aikp.modules.campaign.application.response.CampaignResponse;
import org.afdb.aikp.modules.campaign.application.response.CampaignSummary;
import org.afdb.aikp.modules.campaign.domain.model.Campaign;

public final class CampaignApplicationMapper {

    private CampaignApplicationMapper() {
        // Utility class.
    }

    public static CampaignResponse toResponse(Campaign campaign) {

        if (campaign == null) {
            throw new IllegalArgumentException(
                    "Campaign cannot be null.");
        }

        return new CampaignResponse(
                campaign.getId().getValue(),
                campaign.getCode().getValue(),
                campaign.getName().getValue(),
                campaign.getDescription().getValue(),
                campaign.getStartDate(),
                campaign.getEndDate(),
                campaign.getStatus(),
                campaign.isActive());
    }

    public static CampaignSummary toSummary(Campaign campaign) {

        if (campaign == null) {
            throw new IllegalArgumentException(
                    "Campaign cannot be null.");
        }

        return new CampaignSummary(
                campaign.getId().getValue(),
                campaign.getCode().getValue(),
                campaign.getName().getValue(),
                campaign.getStartDate(),
                campaign.getEndDate(),
                campaign.getStatus(),
                campaign.isActive());
    }
}
