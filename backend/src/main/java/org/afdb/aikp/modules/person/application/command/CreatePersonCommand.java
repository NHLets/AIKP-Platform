package org.afdb.aikp.modules.person.application.command;

import java.util.UUID;

/**
 * Command to create a person officially associated
 * with an organization.
 */
public record CreatePersonCommand(
        String fullName,
        UUID organizationId) {
}
