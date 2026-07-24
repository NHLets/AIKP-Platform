package org.afdb.aikp.modules.country.domain.service;

import org.afdb.aikp.modules.country.domain.valueobject.CountryId;
import org.afdb.aikp.modules.country.domain.valueobject.Iso2Code;
import org.afdb.aikp.modules.country.domain.valueobject.Iso3Code;
import org.afdb.aikp.modules.country.domain.valueobject.NumericCode;

/**
 * Domain service for Country aggregate.
 */
public interface CountryDomainService {

    /**
     * Validates that a country can be created.
     */
    void validateCreation(
            Iso2Code iso2Code,
            Iso3Code iso3Code,
            NumericCode numericCode);

    /**
     * Validates that a country can be updated.
     */
    void validateUpdate(
            CountryId countryId,
            Iso2Code iso2Code,
            Iso3Code iso3Code,
            NumericCode numericCode);
}