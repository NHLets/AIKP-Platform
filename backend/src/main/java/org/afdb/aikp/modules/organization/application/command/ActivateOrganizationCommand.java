package org.afdb.aikp.modules.organization.application.command;

import java.util.UUID;

/**
 * Command used to activate an Organization.
 */
public record ActivateOrganizationCommand(
        UUID id
) {
}
