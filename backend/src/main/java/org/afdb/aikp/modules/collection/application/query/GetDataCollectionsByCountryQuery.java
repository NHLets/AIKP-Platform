package org.afdb.aikp.modules.collection.application.query;

import java.util.UUID;

/**
 * Query to retrieve DataCollections for a country.
 */
public record GetDataCollectionsByCountryQuery(
        UUID countryId) {
}
