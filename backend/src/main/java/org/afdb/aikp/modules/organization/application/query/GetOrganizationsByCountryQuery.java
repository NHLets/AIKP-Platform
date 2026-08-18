package org.afdb.aikp.modules.organization.application.query;

import java.util.UUID;

/**
 * Query used to retrieve Organizations belonging to a country.
 */
public record GetOrganizationsByCountryQuery(
        UUID countryId
) {
}
