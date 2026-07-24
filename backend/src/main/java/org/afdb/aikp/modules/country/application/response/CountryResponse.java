package org.afdb.aikp.modules.country.application.response;

import java.util.UUID;

/**
 * Full country representation returned by the application layer.
 */
public record CountryResponse(

        UUID id,

        String iso2Code,

        String iso3Code,

        String numericCode,

        String name,

        String officialName,

        boolean active

) {
}