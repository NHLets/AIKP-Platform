package org.afdb.aikp.modules.organization.application.command;

import java.util.UUID;

/**
 * Command used to deactivate an Organization.
 */
public record DeactivateOrganizationCommand(
        UUID id
) {
}
