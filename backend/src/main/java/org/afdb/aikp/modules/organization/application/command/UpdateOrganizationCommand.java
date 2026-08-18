package org.afdb.aikp.modules.organization.application.command;

import java.util.UUID;

/**
 * Command used to update an Organization.
 */
public record UpdateOrganizationCommand(
        UUID id,
        String code,
        String name,
        String type,
        UUID countryId
) {
}
