package org.afdb.aikp.modules.organization.application.command;

import java.util.UUID;

/**
 * Command used to create an Organization.
 */
public record CreateOrganizationCommand(
        String code,
        String name,
        String type,
        UUID countryId
) {
}
