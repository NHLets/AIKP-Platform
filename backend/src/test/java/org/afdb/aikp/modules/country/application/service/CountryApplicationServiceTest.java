package org.afdb.aikp.modules.country.application.service;

import org.afdb.aikp.modules.country.application.command.ActivateCountryCommand;
import org.afdb.aikp.modules.country.application.command.CreateCountryCommand;
import org.afdb.aikp.modules.country.application.command.DeactivateCountryCommand;
import org.afdb.aikp.modules.country.application.command.DeleteCountryCommand;
import org.afdb.aikp.modules.country.application.command.UpdateCountryCommand;
import org.afdb.aikp.modules.country.application.query.GetActiveCountriesQuery;
import org.afdb.aikp.modules.country.application.query.GetCountriesQuery;
import org.afdb.aikp.modules.country.application.query.GetCountryQuery;
import org.afdb.aikp.modules.country.application.response.CountryResponse;
import org.afdb.aikp.modules.country.application.response.CountrySummary;
import org.afdb.aikp.modules.country.domain.exception.CountryNotFoundException;
import org.afdb.aikp.modules.country.domain.model.Country;
import org.afdb.aikp.modules.country.domain.repository.CountryRepository;
import org.afdb.aikp.modules.country.domain.service.CountryDomainService;
import org.afdb.aikp.modules.country.domain.valueobject.CountryId;
import org.afdb.aikp.modules.country.domain.valueobject.CountryName;
import org.afdb.aikp.modules.country.domain.valueobject.Iso2Code;
import org.afdb.aikp.modules.country.domain.valueobject.Iso3Code;
import org.afdb.aikp.modules.country.domain.valueobject.NumericCode;
import org.afdb.aikp.modules.country.domain.valueobject.OfficialCountryName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CountryApplicationServiceTest {

    private CountryRepository repository;

    private CountryDomainService domainService;

    private CountryApplicationService service;

    @BeforeEach
    void setUp() {

        repository = mock(CountryRepository.class);
        domainService = mock(CountryDomainService.class);

        service = new CountryApplicationService(
                repository,
                domainService);
    }

    @Test
    void shouldGetCountry() {

        Country country = createCountry();

        when(repository.findById(country.getCountryId()))
                .thenReturn(Optional.of(country));

        CountryResponse response =
                service.get(
                        new GetCountryQuery(
                                country.getCountryId().getValue()));

        assertNotNull(response);
        assertEquals(
                country.getCountryId().getValue(),
                response.id());
        assertEquals("MG", response.iso2Code());
        assertEquals("MDG", response.iso3Code());
        assertEquals("450", response.numericCode());
        assertEquals("Madagascar", response.name());
        assertEquals(
                "Republic of Madagascar",
                response.officialName());
        assertTrue(response.active());

        verify(repository).findById(country.getCountryId());
    }

    @Test
    void shouldThrowWhenCountryDoesNotExist() {

        UUID id = UUID.randomUUID();

        when(repository.findById(CountryId.of(id)))
                .thenReturn(Optional.empty());

        assertThrows(
                CountryNotFoundException.class,
                () -> service.get(
                        new GetCountryQuery(id)));

        verify(repository).findById(CountryId.of(id));
    }

    @Test
    void shouldGetAllCountries() {

        Country madagascar = createCountry();

        Country southAfrica = Country.create(
                CountryId.of(UUID.randomUUID()),
                Iso2Code.of("ZA"),
                Iso3Code.of("ZAF"),
                NumericCode.of("710"),
                CountryName.of("South Africa"),
                OfficialCountryName.of(
                        "Republic of South Africa"));

        when(repository.findAll())
                .thenReturn(List.of(
                        madagascar,
                        southAfrica));

        List<CountrySummary> result =
                service.getAll(
                        new GetCountriesQuery());

        assertEquals(2, result.size());

        assertEquals(
                "MG",
                result.get(0).iso2Code());

        assertEquals(
                "ZA",
                result.get(1).iso2Code());

        verify(repository).findAll();
    }

    @Test
    void shouldGetActiveCountries() {

        Country country = createCountry();

        when(repository.findActive())
                .thenReturn(List.of(country));

        List<CountrySummary> result =
                service.getActive(
                        new GetActiveCountriesQuery());

        assertEquals(1, result.size());
        assertEquals(
                "MG",
                result.get(0).iso2Code());
        assertTrue(result.get(0).active());

        verify(repository).findActive();
    }

    @Test
    void shouldCreateCountry() {

        UUID id = UUID.randomUUID();

        Country savedCountry = Country.create(
                CountryId.of(id),
                Iso2Code.of("MG"),
                Iso3Code.of("MDG"),
                NumericCode.of("450"),
                CountryName.of("Madagascar"),
                OfficialCountryName.of(
                        "Republic of Madagascar"));

        when(repository.save(any(Country.class)))
                .thenReturn(savedCountry);

        CreateCountryCommand command =
                new CreateCountryCommand(
                        "MG",
                        "MDG",
                        "450",
                        "Madagascar",
                        "Republic of Madagascar");

        CountryResponse response =
                service.create(command);

        assertNotNull(response);
        assertEquals(id, response.id());
        assertEquals("MG", response.iso2Code());
        assertEquals("MDG", response.iso3Code());
        assertEquals("450", response.numericCode());
        assertEquals("Madagascar", response.name());
        assertEquals(
                "Republic of Madagascar",
                response.officialName());
        assertTrue(response.active());

        verify(domainService).validateCreation(
                Iso2Code.of("MG"),
                Iso3Code.of("MDG"),
                NumericCode.of("450"));

        verify(repository).save(any(Country.class));
    }

    @Test
    void shouldUpdateCountry() {

        CountryId id =
                CountryId.of(UUID.randomUUID());

        Country country = Country.create(
                id,
                Iso2Code.of("MG"),
                Iso3Code.of("MDG"),
                NumericCode.of("450"),
                CountryName.of("Madagascar"),
                OfficialCountryName.of(
                        "Republic of Madagascar"));

        when(repository.findById(id))
                .thenReturn(Optional.of(country));

        when(repository.save(country))
                .thenReturn(country);

        UpdateCountryCommand command =
                new UpdateCountryCommand(
                        id.getValue(),
                        "ZA",
                        "ZAF",
                        "710",
                        "South Africa",
                        "Republic of South Africa");

        CountryResponse response =
                service.update(command);

        assertNotNull(response);
        assertEquals(id.getValue(), response.id());
        assertEquals("ZA", response.iso2Code());
        assertEquals("ZAF", response.iso3Code());
        assertEquals("710", response.numericCode());
        assertEquals("South Africa", response.name());
        assertEquals(
                "Republic of South Africa",
                response.officialName());

        verify(domainService).validateUpdate(
                id,
                Iso2Code.of("ZA"),
                Iso3Code.of("ZAF"),
                NumericCode.of("710"));

        verify(repository).save(country);
    }

    @Test
    void shouldActivateCountry() {

        Country country = createCountry();

        country.deactivate();

        when(repository.findById(country.getCountryId()))
                .thenReturn(Optional.of(country));

        when(repository.save(country))
                .thenReturn(country);

        CountryResponse response =
                service.activate(
                        new ActivateCountryCommand(
                                country.getCountryId().getValue()));

        assertTrue(response.active());

        verify(repository).findById(country.getCountryId());
        verify(repository).save(country);
    }

    @Test
    void shouldDeactivateCountry() {

        Country country = createCountry();

        when(repository.findById(country.getCountryId()))
                .thenReturn(Optional.of(country));

        when(repository.save(country))
                .thenReturn(country);

        CountryResponse response =
                service.deactivate(
                        new DeactivateCountryCommand(
                                country.getCountryId().getValue()));

        assertFalse(response.active());

        verify(repository).findById(country.getCountryId());
        verify(repository).save(country);
    }

    @Test
    void shouldDeleteCountry() {

        Country country = createCountry();

        when(repository.findById(country.getCountryId()))
                .thenReturn(Optional.of(country));

        service.delete(
                new DeleteCountryCommand(
                        country.getCountryId().getValue()));

        verify(repository).findById(country.getCountryId());
        verify(repository).delete(country);
    }

    @Test
    void shouldThrowWhenUpdatingNonExistingCountry() {

        UUID id = UUID.randomUUID();

        when(repository.findById(CountryId.of(id)))
                .thenReturn(Optional.empty());

        UpdateCountryCommand command =
                new UpdateCountryCommand(
                        id,
                        "MG",
                        "MDG",
                        "450",
                        "Madagascar",
                        "Republic of Madagascar");

        assertThrows(
                CountryNotFoundException.class,
                () -> service.update(command));

        verify(repository).findById(CountryId.of(id));
        verify(repository, never()).save(any());
    }

    @Test
    void shouldThrowWhenDeletingNonExistingCountry() {

        UUID id = UUID.randomUUID();

        when(repository.findById(CountryId.of(id)))
                .thenReturn(Optional.empty());

        assertThrows(
                CountryNotFoundException.class,
                () -> service.delete(
                        new DeleteCountryCommand(id)));

        verify(repository).findById(CountryId.of(id));
        verify(repository, never()).delete(any());
    }

    private Country createCountry() {

        return Country.create(
                CountryId.of(UUID.randomUUID()),
                Iso2Code.of("MG"),
                Iso3Code.of("MDG"),
                NumericCode.of("450"),
                CountryName.of("Madagascar"),
                OfficialCountryName.of(
                        "Republic of Madagascar"));
    }
}
