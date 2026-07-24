package org.afdb.aikp.modules.country.application.response;

import java.util.UUID;

/**
 * Lightweight country representation used in collections.
 */
public record CountrySummary(

        UUID id,

        String iso2Code,

        String iso3Code,

        String numericCode,

        String name,

        String officialName,

        boolean active

) {
}