package org.afdb.aikp.modules.organization.infrastructure.persistence.adapter;

import org.afdb.aikp.modules.country.domain.valueobject.CountryId;
import org.afdb.aikp.modules.organization.domain.model.Organization;
import org.afdb.aikp.modules.organization.domain.repository.OrganizationRepository;
import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationCode;
import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationId;
import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationName;
import org.afdb.aikp.modules.organization.domain.enums.OrganizationType;
import org.afdb.aikp.modules.organization.infrastructure.persistence.repository.OrganizationJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.afdb.aikp.modules.country.infrastructure.persistence.entity.CountryEntity;
import org.afdb.aikp.modules.country.infrastructure.persistence.repository.CountryJpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
class OrganizationRepositoryAdapterIntegrationTest {

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
    private OrganizationRepositoryAdapter adapter;

    @Autowired
    private OrganizationJpaRepository jpaRepository;

    @Autowired
    private CountryJpaRepository countryJpaRepository;

    @Autowired
    private OrganizationRepository repository;

    @BeforeEach
    void cleanDatabase() {

    jpaRepository.deleteAll();
    countryJpaRepository.deleteAll();

    countryJpaRepository.save(
            new CountryEntity(
                    countryId().getValue(),
                    "MG",
                    "MDG",
                    "450",
                    "Madagascar",
                    "Republic of Madagascar",
                    true
            )
    );
    }

    @Test
    void shouldSaveAndFindById() {

        Organization organization =
                createOrganization();

        Organization saved =
                adapter.save(organization);

        Optional<Organization> result =
                adapter.findById(
                        saved.getOrganizationId());

        assertTrue(result.isPresent());
        assertEquals(
                saved.getOrganizationId(),
                result.get().getOrganizationId());

        assertEquals(
                "AFDB",
                result.get().getCode().getValue());

        assertEquals(
                "African Development Bank",
                result.get().getName().getValue());

        assertEquals(
                OrganizationType.GOVERNMENT_AGENCY,
                result.get().getType());

        assertEquals(
                countryId(),
                result.get().getCountryId());

        assertTrue(result.get().isActive());
    }

    @Test
    void shouldFindByCode() {

        Organization organization =
                createOrganization();

        adapter.save(organization);

        Optional<Organization> result =
                adapter.findByCode(
                        OrganizationCode.of("AFDB"));

        assertTrue(result.isPresent());

        assertEquals(
                organization.getOrganizationId(),
                result.get().getOrganizationId());
    }

    @Test
    void shouldFindByCountryId() {

        Organization organization =
                createOrganization();

        adapter.save(organization);

        List<Organization> result =
                adapter.findByCountryId(
                        countryId());

        assertEquals(1, result.size());

        assertEquals(
                organization.getOrganizationId(),
                result.get(0).getOrganizationId());
    }

    @Test
    void shouldFindByType() {

        Organization organization =
                createOrganization();

        adapter.save(organization);

        List<Organization> result =
                adapter.findByType(
                        OrganizationType.GOVERNMENT_AGENCY);

        assertEquals(1, result.size());

        assertEquals(
                organization.getOrganizationId(),
                result.get(0).getOrganizationId());
    }

    @Test
    void shouldFindAllOrganizations() {

        Organization afdb =
                createOrganization();

        Organization comesa =
                Organization.restore(
                        OrganizationId.generate(),
                        OrganizationCode.of("COMESA"),
                        OrganizationName.of(
                                "Common Market for Eastern and Southern Africa"),
                        OrganizationType.MINISTRY,
                        countryId(),
                        true);

        adapter.save(afdb);
        adapter.save(comesa);

        List<Organization> result =
                adapter.findAll();

        assertEquals(2, result.size());
    }

    @Test
    void shouldFindOnlyActiveOrganizations() {

        Organization activeOrganization =
                createOrganization();

        Organization inactiveOrganization =
                Organization.restore(
                        OrganizationId.generate(),
                        OrganizationCode.of("COMESA"),
                        OrganizationName.of(
                                "Common Market for Eastern and Southern Africa"),
                        OrganizationType.MINISTRY,
                        countryId(),
                        false);

        adapter.save(activeOrganization);
        adapter.save(inactiveOrganization);

        List<Organization> result =
                adapter.findActive();

        assertEquals(1, result.size());

        assertEquals(
                "AFDB",
                result.get(0).getCode().getValue());
    }

    @Test
    void shouldCheckExistenceById() {

        Organization organization =
                createOrganization();

        adapter.save(organization);

        assertTrue(
                adapter.existsById(
                        organization.getOrganizationId()));

        assertFalse(
                adapter.existsById(
                        OrganizationId.generate()));
    }

    @Test
    void shouldCheckExistenceByCode() {

        Organization organization =
                createOrganization();

        adapter.save(organization);

        assertTrue(
                adapter.existsByCode(
                        OrganizationCode.of("AFDB")));

        assertFalse(
                adapter.existsByCode(
                        OrganizationCode.of("COMESA")));
    }

    @Test
    void shouldDeleteOrganization() {

        Organization organization =
                createOrganization();

        adapter.save(organization);

        assertTrue(
                adapter.existsById(
                        organization.getOrganizationId()));

        adapter.delete(organization);

        assertFalse(
                adapter.existsById(
                        organization.getOrganizationId()));

        assertTrue(
                adapter.findById(
                        organization.getOrganizationId()).isEmpty());
    }

    private Organization createOrganization() {

        return Organization.restore(
                OrganizationId.generate(),
                OrganizationCode.of("AFDB"),
                OrganizationName.of(
                        "African Development Bank"),
                OrganizationType.GOVERNMENT_AGENCY,
                countryId(),
                true);
    }

    private CountryId countryId() {

        return CountryId.of(
                UUID.fromString(
                        "11111111-1111-1111-1111-111111111111"));
    }
}
