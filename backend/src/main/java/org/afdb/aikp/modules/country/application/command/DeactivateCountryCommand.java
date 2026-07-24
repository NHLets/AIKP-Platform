package org.afdb.aikp.modules.country.application.command;

import java.util.UUID;

/**
 * Deactivates a country.
 */
public record DeactivateCountryCommand(UUID id) {
}