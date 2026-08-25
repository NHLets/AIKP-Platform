package org.afdb.aikp.modules.person.application.response;

import java.util.UUID;

/**
 * Lightweight representation of a Person.
 */
public record PersonSummary(
        UUID id,
        String fullName,
        UUID organizationId,
        boolean active) {
}
