package org.afdb.aikp.modules.campaign.application.query;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record GetCampaignQuery(

        @NotNull
        UUID id

) {
}
