package org.afdb.aikp.modules.person.application.command;

import java.util.UUID;

/**
 * Command to delete a person.
 */
public record DeletePersonCommand(
        UUID personId) {
}
