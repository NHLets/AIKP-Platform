package org.afdb.aikp.modules.country.infrastructure.persistence.adapter;

import org.afdb.aikp.modules.country.domain.model.Country;
import org.afdb.aikp.modules.country.domain.repository.CountryRepository;
import org.afdb.aikp.modules.country.domain.valueobject.CountryId;
import org.afdb.aikp.modules.country.domain.valueobject.CountryName;
import org.afdb.aikp.modules.country.domain.valueobject.Iso2Code;
import org.afdb.aikp.modules.country.domain.valueobject.Iso3Code;
import org.afdb.aikp.modules.country.domain.valueobject.NumericCode;
import org.afdb.aikp.modules.country.domain.valueobject.OfficialCountryName;
import org.afdb.aikp.modules.country.infrastructure.persistence.repository.CountryJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
class CountryRepositoryAdapterIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:17-alpine")
                    .withDatabaseName("aikp")
                    .withUsername("postgres")
                    .withPassword("postgres");

    @DynamicPropertySource
    static void configureDatabase(
            DynamicPropertyRegistry registry) {

        registry.add(
                "spring.datasource.url",
                postgres::getJdbcUrl);

        registry.add(
                "spring.datasource.username",
                postgres::getUsername);

        registry.add(
                "spring.datasource.password",
                postgres::getPassword);

        registry.add(
                "spring.datasource.driver-class-name",
                postgres::getDriverClassName);
    }

    @Autowired
    private CountryRepositoryAdapter adapter;

    @Autowired
    private CountryJpaRepository jpaRepository;

    @BeforeEach
    void cleanDatabase() {
        jpaRepository.deleteAll();
    }

    @Test
    void shouldSaveAndFindById() {

        Country country = createCountry();

        Country saved = adapter.save(country);

        Optional<Country> result =
                adapter.findById(saved.getCountryId());

        assertTrue(result.isPresent());
        assertEquals(saved.getCountryId(), result.get().getCountryId());
        assertEquals("MG", result.get().getIso2Code().getValue());
        assertEquals("MDG", result.get().getIso3Code().getValue());
        assertEquals("450", result.get().getNumericCode().getValue());
        assertEquals("Madagascar", result.get().getName().getValue());
        assertTrue(result.get().isActive());
    }

    @Test
    void shouldFindByIso2Code() {

        Country country = createCountry();

        adapter.save(country);

        Optional<Country> result =
                adapter.findByIso2Code(
                        Iso2Code.of("MG"));

        assertTrue(result.isPresent());
        assertEquals(
                country.getCountryId(),
                result.get().getCountryId());
    }

    @Test
    void shouldFindByIso3Code() {

        Country country = createCountry();

        adapter.save(country);

        Optional<Country> result =
                adapter.findByIso3Code(
                        Iso3Code.of("MDG"));

        assertTrue(result.isPresent());
        assertEquals(
                country.getCountryId(),
                result.get().getCountryId());
    }

    @Test
    void shouldFindByNumericCode() {

        Country country = createCountry();

        adapter.save(country);

        Optional<Country> result =
                adapter.findByNumericCode(
                        NumericCode.of("450"));

        assertTrue(result.isPresent());
        assertEquals(
                country.getCountryId(),
                result.get().getCountryId());
    }

    @Test
    void shouldFindAllCountries() {

        Country madagascar = createCountry();

        Country southAfrica = Country.create(
                CountryId.of(UUID.randomUUID()),
                Iso2Code.of("ZA"),
                Iso3Code.of("ZAF"),
                NumericCode.of("710"),
                CountryName.of("South Africa"),
                OfficialCountryName.of(
                        "Republic of South Africa"));

        adapter.save(madagascar);
        adapter.save(southAfrica);

        List<Country> result = adapter.findAll();

        assertEquals(2, result.size());
    }

    @Test
    void shouldFindOnlyActiveCountries() {

        Country activeCountry = createCountry();

        Country inactiveCountry = Country.create(
                CountryId.of(UUID.randomUUID()),
                Iso2Code.of("ZA"),
                Iso3Code.of("ZAF"),
                NumericCode.of("710"),
                CountryName.of("South Africa"),
                OfficialCountryName.of(
                        "Republic of South Africa"));

        inactiveCountry.deactivate();

        adapter.save(activeCountry);
        adapter.save(inactiveCountry);

        List<Country> result =
                adapter.findActive();

        assertEquals(1, result.size());
        assertEquals(
                "MG",
                result.get(0).getIso2Code().getValue());
    }

    @Test
    void shouldCheckExistenceById() {

        Country country = createCountry();

        adapter.save(country);

        assertTrue(
                adapter.existsById(
                        country.getCountryId()));

        assertFalse(
                adapter.existsById(
                        CountryId.of(UUID.randomUUID())));
    }

    @Test
    void shouldCheckExistenceByIsoCodes() {

        Country country = createCountry();

        adapter.save(country);

        assertTrue(
                adapter.existsByIso2Code(
                        Iso2Code.of("MG")));

        assertTrue(
                adapter.existsByIso3Code(
                        Iso3Code.of("MDG")));

        assertTrue(
                adapter.existsByNumericCode(
                        NumericCode.of("450")));

        assertFalse(
                adapter.existsByIso2Code(
                        Iso2Code.of("ZA")));

        assertFalse(
                adapter.existsByIso3Code(
                        Iso3Code.of("ZAF")));

        assertFalse(
                adapter.existsByNumericCode(
                        NumericCode.of("710")));
    }

    @Test
    void shouldDeleteCountry() {

        Country country = createCountry();

        adapter.save(country);

        assertTrue(
                adapter.existsById(
                        country.getCountryId()));

        adapter.delete(country);

        assertFalse(
                adapter.existsById(
                        country.getCountryId()));

        assertTrue(
                adapter.findById(
                        country.getCountryId()).isEmpty());
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
