package org.afdb.aikp.modules.organization.application.command;

import java.util.UUID;

/**
 * Command used to delete an Organization.
 */
public record DeleteOrganizationCommand(
        UUID id
) {
}
