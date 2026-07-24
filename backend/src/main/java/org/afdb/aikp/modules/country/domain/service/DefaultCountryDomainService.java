package org.afdb.aikp.modules.country.domain.service;

import org.afdb.aikp.modules.country.domain.exception.DuplicateCountryException;
import org.afdb.aikp.modules.country.domain.exception.InvalidCountryException;
import org.afdb.aikp.modules.country.domain.repository.CountryRepository;
import org.afdb.aikp.modules.country.domain.valueobject.CountryId;
import org.afdb.aikp.modules.country.domain.valueobject.Iso2Code;
import org.afdb.aikp.modules.country.domain.valueobject.Iso3Code;
import org.afdb.aikp.modules.country.domain.valueobject.NumericCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of the Country domain service.
 *
 * <p>
 * Responsible for enforcing business rules that involve the Country
 * repository and cannot naturally belong to the aggregate itself.
 * </p>
 */
@Service
@Transactional(readOnly = true)
public class DefaultCountryDomainService implements CountryDomainService {

    private final CountryRepository repository;

    public DefaultCountryDomainService(CountryRepository repository) {
        this.repository = repository;
    }

    @Override
    public void validateCreation(
            Iso2Code iso2Code,
            Iso3Code iso3Code,
            NumericCode numericCode) {

        validateIsoCodes(iso2Code, iso3Code, numericCode);

        repository.findByIso2Code(iso2Code)
                .ifPresent(country -> {
                    throw new DuplicateCountryException(
                            "A country already exists with ISO2 code "
                                    + iso2Code.getValue());
                });

        repository.findByIso3Code(iso3Code)
                .ifPresent(country -> {
                    throw new DuplicateCountryException(
                            "A country already exists with ISO3 code "
                                    + iso3Code.getValue());
                });

        repository.findByNumericCode(numericCode)
                .ifPresent(country -> {
                    throw new DuplicateCountryException(
                            "A country already exists with numeric code "
                                    + numericCode.getValue());
                });
    }

    @Override
    public void validateUpdate(
            CountryId countryId,
            Iso2Code iso2Code,
            Iso3Code iso3Code,
            NumericCode numericCode) {

        validateIsoCodes(iso2Code, iso3Code, numericCode);

        repository.findByIso2Code(iso2Code)
                .filter(country -> !country.getId().equals(countryId))
                .ifPresent(country -> {
                    throw new DuplicateCountryException(
                            "ISO2 code already used by another country.");
                });

        repository.findByIso3Code(iso3Code)
                .filter(country -> !country.getId().equals(countryId))
                .ifPresent(country -> {
                    throw new DuplicateCountryException(
                            "ISO3 code already used by another country.");
                });

        repository.findByNumericCode(numericCode)
                .filter(country -> !country.getId().equals(countryId))
                .ifPresent(country -> {
                    throw new DuplicateCountryException(
                            "Numeric code already used by another country.");
                });
    }

    /**
     * Common business validation.
     */
    private void validateIsoCodes(
            Iso2Code iso2Code,
            Iso3Code iso3Code,
            NumericCode numericCode) {

        if (iso2Code == null) {
            throw new InvalidCountryException("ISO2 code cannot be null.");
        }

        if (iso3Code == null) {
            throw new InvalidCountryException("ISO3 code cannot be null.");
        }

        if (numericCode == null) {
            throw new InvalidCountryException("Numeric code cannot be null.");
        }
    }

}