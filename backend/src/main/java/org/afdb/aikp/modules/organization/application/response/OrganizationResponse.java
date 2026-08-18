package org.afdb.aikp.modules.organization.application.response;

import java.util.UUID;

/**
 * Detailed application response for an Organization.
 */
public record OrganizationResponse(
        UUID id,
        String code,
        String name,
        String type,
        UUID countryId,
        boolean active
) {
}
