package org.afdb.aikp.modules.campaign.application.response;

import org.afdb.aikp.modules.campaign.domain.enums.CampaignStatus;

import java.time.LocalDate;
import java.util.UUID;

public record CampaignResponse(
        UUID id,
        String code,
        String name,
        String description,
        LocalDate startDate,
        LocalDate endDate,
        CampaignStatus status,
        boolean active
) {
}
