package org.afdb.aikp.modules.country.application.command;

import java.util.UUID;

/**
 * Activates a country.
 */
public record ActivateCountryCommand(UUID id) {
}