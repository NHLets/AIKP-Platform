package org.afdb.aikp.modules.person.application.command;

import java.util.UUID;

/**
 * Command to update a person's official information.
 */
public record UpdatePersonCommand(
        UUID personId,
        String fullName,
        UUID organizationId) {
}
