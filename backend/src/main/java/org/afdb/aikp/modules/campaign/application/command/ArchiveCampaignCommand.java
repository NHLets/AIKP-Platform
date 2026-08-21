package org.afdb.aikp.modules.campaign.application.command;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ArchiveCampaignCommand(

        @NotNull
        UUID id

) {
}
