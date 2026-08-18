package org.afdb.aikp.modules.organization.application.response;

import java.util.UUID;

/**
 * Summary application response for an Organization.
 */
public record OrganizationSummary(
        UUID id,
        String code,
        String name,
        String type,
        UUID countryId,
        boolean active
) {
}
