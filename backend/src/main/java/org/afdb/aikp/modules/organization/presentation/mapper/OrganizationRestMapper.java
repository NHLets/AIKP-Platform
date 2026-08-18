package org.afdb.aikp.modules.organization.presentation.mapper;

import org.afdb.aikp.modules.organization.application.command.CreateOrganizationCommand;
import org.afdb.aikp.modules.organization.application.command.UpdateOrganizationCommand;
import org.afdb.aikp.modules.organization.presentation.request.CreateOrganizationRequest;
import org.afdb.aikp.modules.organization.presentation.request.UpdateOrganizationRequest;

import java.util.UUID;

/**
 * Maps REST requests to application commands.
 */
public final class OrganizationRestMapper {

    private OrganizationRestMapper() {
        // Utility class
    }

    /**
     * Maps a create request to an application command.
     */
    public static CreateOrganizationCommand toCommand(
            CreateOrganizationRequest request) {

        return new CreateOrganizationCommand(
                request.code(),
                request.name(),
                request.type(),
                request.countryId()
        );
    }

    /**
     * Maps an update request to an application command.
     */
    public static UpdateOrganizationCommand toCommand(
            UUID id,
            UpdateOrganizationRequest request) {

        return new UpdateOrganizationCommand(
                id,
                request.code(),
                request.name(),
                request.type(),
                request.countryId()
        );
    }
}
