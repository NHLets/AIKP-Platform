package org.afdb.aikp.modules.person.application.response;

import java.util.UUID;

/**
 * Application response representing a person.
 */
public record PersonResponse(
        UUID id,
        String fullName,
        UUID organizationId,
        boolean active) {
}
