package org.afdb.aikp.modules.campaign.application.command;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CompleteCampaignCommand(

        @NotNull
        UUID id

) {
}
