package org.afdb.aikp.modules.campaign.presentation.mapper;

import org.afdb.aikp.modules.campaign.application.command.CreateCampaignCommand;
import org.afdb.aikp.modules.campaign.application.command.UpdateCampaignCommand;
import org.afdb.aikp.modules.campaign.presentation.request.CreateCampaignRequest;
import org.afdb.aikp.modules.campaign.presentation.request.UpdateCampaignRequest;

import java.util.UUID;

/**
 * Maps REST requests to Campaign application commands.
 */
public final class CampaignRestMapper {

    private CampaignRestMapper() {
        // Utility class.
    }

    public static CreateCampaignCommand toCommand(
            CreateCampaignRequest request) {

        if (request == null) {
            throw new IllegalArgumentException(
                    "Create campaign request cannot be null.");
        }

        return new CreateCampaignCommand(
                request.code(),
                request.name(),
                request.description(),
                request.startDate(),
                request.endDate());
    }

    public static UpdateCampaignCommand toCommand(
            UUID id,
            UpdateCampaignRequest request) {

        if (id == null) {
            throw new IllegalArgumentException(
                    "Campaign id cannot be null.");
        }

        if (request == null) {
            throw new IllegalArgumentException(
                    "Update campaign request cannot be null.");
        }

        return new UpdateCampaignCommand(
                id,
                request.name(),
                request.description(),
                request.startDate(),
                request.endDate());
    }
}
