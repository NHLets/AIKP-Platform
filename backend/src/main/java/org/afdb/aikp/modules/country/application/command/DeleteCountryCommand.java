package org.afdb.aikp.modules.country.application.command;

import java.util.UUID;

/**
 * Deletes a country.
 */
public record DeleteCountryCommand(UUID id) {
}