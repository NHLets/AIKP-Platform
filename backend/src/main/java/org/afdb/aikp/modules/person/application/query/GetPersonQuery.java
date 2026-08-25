package org.afdb.aikp.modules.person.application.query;

import java.util.UUID;

/**
 * Query to retrieve a person by identifier.
 */
public record GetPersonQuery(
        UUID personId) {
}
