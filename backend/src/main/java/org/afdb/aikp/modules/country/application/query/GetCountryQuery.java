package org.afdb.aikp.modules.country.application.query;

import java.util.UUID;

/**
 * Query used to retrieve a country by its identifier.
 */
public record GetCountryQuery(

        UUID id

) {
}