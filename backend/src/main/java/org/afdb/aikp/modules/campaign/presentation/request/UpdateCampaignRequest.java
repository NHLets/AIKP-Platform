package org.afdb.aikp.modules.campaign.presentation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UpdateCampaignRequest(

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
