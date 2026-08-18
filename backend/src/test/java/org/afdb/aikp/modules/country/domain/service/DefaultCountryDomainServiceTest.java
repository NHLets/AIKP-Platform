package org.afdb.aikp.modules.country.domain.service;

import org.afdb.aikp.modules.country.domain.exception.DuplicateCountryException;
import org.afdb.aikp.modules.country.domain.exception.InvalidCountryException;
import org.afdb.aikp.modules.country.domain.model.Country;
import org.afdb.aikp.modules.country.domain.repository.CountryRepository;
import org.afdb.aikp.modules.country.domain.valueobject.CountryId;
import org.afdb.aikp.modules.country.domain.valueobject.CountryName;
import org.afdb.aikp.modules.country.domain.valueobject.Iso2Code;
import org.afdb.aikp.modules.country.domain.valueobject.Iso3Code;
import org.afdb.aikp.modules.country.domain.valueobject.NumericCode;
import org.afdb.aikp.modules.country.domain.valueobject.OfficialCountryName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DefaultCountryDomainServiceTest {

    private CountryRepository repository;

    private DefaultCountryDomainService service;

    @BeforeEach
    void setUp() {

        repository = mock(CountryRepository.class);

        service = new DefaultCountryDomainService(repository);
    }

    @Test
    void shouldValidateCreationWhenCodesAreUnique() {

        Iso2Code iso2 = Iso2Code.of("MG");
        Iso3Code iso3 = Iso3Code.of("MDG");
        NumericCode numeric = NumericCode.of("450");

        when(repository.findByIso2Code(iso2))
                .thenReturn(Optional.empty());

        when(repository.findByIso3Code(iso3))
                .thenReturn(Optional.empty());

        when(repository.findByNumericCode(numeric))
                .thenReturn(Optional.empty());

        assertDoesNotThrow(() ->
                service.validateCreation(
                        iso2,
                        iso3,
                        numeric));
    }

    @Test
    void shouldRejectCreationWhenIso2AlreadyExists() {

        Iso2Code iso2 = Iso2Code.of("MG");
        Iso3Code iso3 = Iso3Code.of("MDG");
        NumericCode numeric = NumericCode.of("450");

        when(repository.findByIso2Code(iso2))
                .thenReturn(Optional.of(mock(Country.class)));

        assertThrows(
                DuplicateCountryException.class,
                () -> service.validateCreation(
                        iso2,
                        iso3,
                        numeric));
    }

    @Test
    void shouldRejectCreationWhenIso3AlreadyExists() {

        Iso2Code iso2 = Iso2Code.of("MG");
        Iso3Code iso3 = Iso3Code.of("MDG");
        NumericCode numeric = NumericCode.of("450");

        when(repository.findByIso2Code(iso2))
                .thenReturn(Optional.empty());

        when(repository.findByIso3Code(iso3))
                .thenReturn(Optional.of(mock(Country.class)));

        assertThrows(
                DuplicateCountryException.class,
                () -> service.validateCreation(
                        iso2,
                        iso3,
                        numeric));
    }

    @Test
    void shouldRejectCreationWhenNumericCodeAlreadyExists() {

        Iso2Code iso2 = Iso2Code.of("MG");
        Iso3Code iso3 = Iso3Code.of("MDG");
        NumericCode numeric = NumericCode.of("450");

        when(repository.findByIso2Code(iso2))
                .thenReturn(Optional.empty());

        when(repository.findByIso3Code(iso3))
                .thenReturn(Optional.empty());

        when(repository.findByNumericCode(numeric))
                .thenReturn(Optional.of(mock(Country.class)));

        assertThrows(
                DuplicateCountryException.class,
                () -> service.validateCreation(
                        iso2,
                        iso3,
                        numeric));
    }

    @Test
    void shouldAllowUpdateWhenCodesBelongToSameCountry() {

        CountryId countryId =
                CountryId.of(UUID.randomUUID());

        Country country = mock(Country.class);

        when(country.getId())
                .thenReturn(countryId);

        Iso2Code iso2 = Iso2Code.of("MG");
        Iso3Code iso3 = Iso3Code.of("MDG");
        NumericCode numeric = NumericCode.of("450");

        when(repository.findByIso2Code(iso2))
                .thenReturn(Optional.of(country));

        when(repository.findByIso3Code(iso3))
                .thenReturn(Optional.of(country));

        when(repository.findByNumericCode(numeric))
                .thenReturn(Optional.of(country));

        assertDoesNotThrow(() ->
                service.validateUpdate(
                        countryId,
                        iso2,
                        iso3,
                        numeric));
    }

    @Test
    void shouldRejectUpdateWhenIso2BelongsToAnotherCountry() {

        CountryId countryId =
                CountryId.of(UUID.randomUUID());

        CountryId anotherCountryId =
                CountryId.of(UUID.randomUUID());

        Country country = mock(Country.class);

        when(country.getId())
                .thenReturn(anotherCountryId);

        Iso2Code iso2 = Iso2Code.of("MG");
        Iso3Code iso3 = Iso3Code.of("MDG");
        NumericCode numeric = NumericCode.of("450");

        when(repository.findByIso2Code(iso2))
                .thenReturn(Optional.of(country));

        assertThrows(
                DuplicateCountryException.class,
                () -> service.validateUpdate(
                        countryId,
                        iso2,
                        iso3,
                        numeric));
    }

    @Test
    void shouldRejectCreationWhenIso2IsNull() {

        Iso3Code iso3 = Iso3Code.of("MDG");
        NumericCode numeric = NumericCode.of("450");

        assertThrows(
                InvalidCountryException.class,
                () -> service.validateCreation(
                        null,
                        iso3,
                        numeric));
    }
}
