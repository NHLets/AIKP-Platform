package org.afdb.aikp.modules.campaign.application.response;

import org.afdb.aikp.modules.campaign.domain.enums.CampaignStatus;

import java.time.LocalDate;
import java.util.UUID;

public record CampaignSummary(
        UUID id,
        String code,
        String name,
        LocalDate startDate,
        LocalDate endDate,
        CampaignStatus status,
        boolean active
) {
}
