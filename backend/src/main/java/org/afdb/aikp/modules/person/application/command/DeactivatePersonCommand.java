package org.afdb.aikp.modules.person.application.command;

import java.util.UUID;

/**
 * Command to deactivate a person.
 */
public record DeactivatePersonCommand(
        UUID personId) {
}
