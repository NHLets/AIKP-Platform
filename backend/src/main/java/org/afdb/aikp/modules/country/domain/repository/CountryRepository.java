package org.afdb.aikp.modules.country.domain.repository;

import org.afdb.aikp.modules.country.domain.model.Country;
import org.afdb.aikp.modules.country.domain.valueobject.CountryId;
import org.afdb.aikp.modules.country.domain.valueobject.Iso2Code;
import org.afdb.aikp.modules.country.domain.valueobject.Iso3Code;
import org.afdb.aikp.modules.country.domain.valueobject.NumericCode;

import java.util.List;
import java.util.Optional;

/**
 * Repository contract for Country aggregates.
 */
public interface CountryRepository {

    /**
     * Persists a country.
     *
     * @param country aggregate to persist
     * @return persisted aggregate
     */
    Country save(Country country);

    /**
     * Finds a country by its identifier.
     */
    Optional<Country> findById(CountryId id);

    /**
     * Finds a country by ISO 3166-1 alpha-2 code.
     */
    Optional<Country> findByIso2Code(Iso2Code iso2Code);

    /**
     * Finds a country by ISO 3166-1 alpha-3 code.
     */
    Optional<Country> findByIso3Code(Iso3Code iso3Code);

    /**
     * Finds a country by ISO 3166-1 numeric code.
     */
    Optional<Country> findByNumericCode(NumericCode numericCode);

    /**
     * Returns all countries.
     */
    List<Country> findAll();

    /**
     * Returns all active countries.
     */
    List<Country> findActive();

    /**
     * Checks whether a country exists.
     */
    boolean existsById(CountryId id);

    /**
     * Checks whether an ISO2 code already exists.
     */
    boolean existsByIso2Code(Iso2Code iso2Code);

    /**
     * Checks whether an ISO3 code already exists.
     */
    boolean existsByIso3Code(Iso3Code iso3Code);

    /**
     * Checks whether a numeric code already exists.
     */
    boolean existsByNumericCode(NumericCode numericCode);

    /**
     * Deletes a country.
     */
    void delete(Country country);

}