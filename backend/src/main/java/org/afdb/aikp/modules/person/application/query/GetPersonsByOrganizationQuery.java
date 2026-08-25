package org.afdb.aikp.modules.person.application.query;

import java.util.UUID;

/**
 * Query to retrieve persons associated
 * with a specific organization.
 */
public record GetPersonsByOrganizationQuery(
        UUID organizationId) {
}
