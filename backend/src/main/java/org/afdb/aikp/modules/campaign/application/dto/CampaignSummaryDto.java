package org.afdb.aikp.modules.campaign.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.afdb.aikp.modules.campaign.domain.enums.CampaignStatus;

@Schema(description = "Campaign summary for selectors")
public record CampaignSummaryDto(

    @Schema(description = "Campaign identifier")
    Long id,

    @Schema(description = "Campaign code")
    String code,

    @Schema(description = "Campaign name")
    String name,

    @Schema(description = "Reference year")
    Integer referenceYear,

    @Schema(description = "Campaign status")
    CampaignStatus status

) {}
