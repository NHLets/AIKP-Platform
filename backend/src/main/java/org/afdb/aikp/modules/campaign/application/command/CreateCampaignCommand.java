package org.afdb.aikp.modules.campaign.application.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateCampaignCommand(

        @NotBlank
        @Size(max = 50)
        String code,

        @NotBlank
        @Size(max = 255)
        String name,

        @NotBlank
        @Size(max = 1000)
        String description,

        @NotNull
        LocalDate startDate,

        @NotNull
        LocalDate endDate

) {
}
