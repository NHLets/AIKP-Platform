package org.afdb.aikp.modules.organization.application.query;

/**
 * Query used to retrieve Organizations by type.
 */
public record GetOrganizationsByTypeQuery(
        String type
) {
}
