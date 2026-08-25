package org.afdb.aikp.modules.person.application.command;

import java.util.UUID;

/**
 * Command to activate a person.
 */
public record ActivatePersonCommand(
        UUID personId) {
}
