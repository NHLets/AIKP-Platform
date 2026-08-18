package org.afdb.aikp.modules.organization.application.query;

import java.util.UUID;

/**
 * Query used to retrieve an Organization by its identifier.
 */
public record GetOrganizationQuery(
        UUID id
) {
}
