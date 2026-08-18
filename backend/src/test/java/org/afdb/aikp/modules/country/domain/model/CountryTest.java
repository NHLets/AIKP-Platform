package org.afdb.aikp.modules.country.domain.model;

import org.afdb.aikp.modules.country.domain.valueobject.CountryId;
import org.afdb.aikp.modules.country.domain.valueobject.CountryName;
import org.afdb.aikp.modules.country.domain.valueobject.Iso2Code;
import org.afdb.aikp.modules.country.domain.valueobject.Iso3Code;
import org.afdb.aikp.modules.country.domain.valueobject.NumericCode;
import org.afdb.aikp.modules.country.domain.valueobject.OfficialCountryName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CountryTest {

    @Test
    void shouldCreateActiveCountry() {

        CountryId id = CountryId.of(UUID.randomUUID());
        Iso2Code iso2 = Iso2Code.of("MG");
        Iso3Code iso3 = Iso3Code.of("MDG");
        NumericCode numeric = NumericCode.of("450");
        CountryName name = CountryName.of("Madagascar");
        OfficialCountryName officialName =
                OfficialCountryName.of("Republic of Madagascar");

        Country country = Country.create(
                id,
                iso2,
                iso3,
                numeric,
                name,
                officialName);

        assertEquals(id, country.getCountryId());
        assertEquals(iso2, country.getIso2Code());
        assertEquals(iso3, country.getIso3Code());
        assertEquals(numeric, country.getNumericCode());
        assertEquals(name, country.getName());
        assertEquals(officialName, country.getOfficialName());
        assertTrue(country.isActive());
    }

    @Test
    void shouldRestoreCountryWithPersistedActiveState() {

        CountryId id = CountryId.of(UUID.randomUUID());
        Iso2Code iso2 = Iso2Code.of("MG");
        Iso3Code iso3 = Iso3Code.of("MDG");
        NumericCode numeric = NumericCode.of("450");
        CountryName name = CountryName.of("Madagascar");
        OfficialCountryName officialName =
                OfficialCountryName.of("Republic of Madagascar");

        Country country = Country.restore(
                id,
                iso2,
                iso3,
                numeric,
                name,
                officialName,
                false);

        assertEquals(id, country.getCountryId());
        assertEquals(iso2, country.getIso2Code());
        assertEquals(iso3, country.getIso3Code());
        assertEquals(numeric, country.getNumericCode());
        assertEquals(name, country.getName());
        assertEquals(officialName, country.getOfficialName());
        assertFalse(country.isActive());
    }

    @Test
    void shouldRenameCountry() {

        Country country = createCountry();

        CountryName newName = CountryName.of("Madagascar Updated");
        OfficialCountryName newOfficialName =
                OfficialCountryName.of("Updated Republic of Madagascar");

        country.rename(
                newName,
                newOfficialName);

        assertEquals(newName, country.getName());
        assertEquals(newOfficialName, country.getOfficialName());
    }

    @Test
    void shouldChangeCountryCodes() {

        Country country = createCountry();

        Iso2Code newIso2 = Iso2Code.of("ZA");
        Iso3Code newIso3 = Iso3Code.of("ZAF");
        NumericCode newNumeric = NumericCode.of("710");

        country.changeCodes(
                newIso2,
                newIso3,
                newNumeric);

        assertEquals(newIso2, country.getIso2Code());
        assertEquals(newIso3, country.getIso3Code());
        assertEquals(newNumeric, country.getNumericCode());
    }

    @Test
    void shouldActivateCountry() {

        Country country = createCountry();

        country.deactivate();

        assertFalse(country.isActive());

        country.activate();

        assertTrue(country.isActive());
    }

    @Test
    void shouldDeactivateCountry() {

        Country country = createCountry();

        assertTrue(country.isActive());

        country.deactivate();

        assertFalse(country.isActive());
    }

    @Test
    void shouldPreserveCountryIdentityWhenChangingBusinessData() {

        CountryId id = CountryId.of(UUID.randomUUID());

        Country country = Country.create(
                id,
                Iso2Code.of("MG"),
                Iso3Code.of("MDG"),
                NumericCode.of("450"),
                CountryName.of("Madagascar"),
                OfficialCountryName.of(
                        "Republic of Madagascar"));

        country.rename(
                CountryName.of("Updated Madagascar"),
                OfficialCountryName.of(
                        "Updated Republic of Madagascar"));

        country.changeCodes(
                Iso2Code.of("ZA"),
                Iso3Code.of("ZAF"),
                NumericCode.of("710"));

        assertEquals(id, country.getCountryId());
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