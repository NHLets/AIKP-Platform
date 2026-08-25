package org.afdb.aikp.modules.organization.infrastructure.persistence.adapter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.afdb.aikp.modules.country.domain.valueobject.CountryId;
import org.afdb.aikp.modules.country.infrastructure.persistence.entity.CountryEntity;
import org.afdb.aikp.modules.country.infrastructure.persistence.repository.CountryJpaRepository;

import org.afdb.aikp.modules.organization.domain.enums.OrganizationType;
import org.afdb.aikp.modules.organization.domain.model.Organization;
import org.afdb.aikp.modules.organization.domain.repository.OrganizationRepository;
import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationCode;
import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationId;
import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationName;
import org.afdb.aikp.modules.organization.infrastructure.persistence.repository.OrganizationJpaRepository;
import org.afdb.aikp.modules.person.infrastructure.persistence.repository.PersonJpaRepository;
import org.afdb.aikp.modules.collection.infrastructure.persistence.repository.DataCollectionJpaRepository;

import org.afdb.aikp.modules.person.infrastructure.persistence.repository.PersonJpaRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrganizationRepositoryAdapterIntegrationTest {

    private static final String TEST_ISO2_CODE = "MG";

    private static final String TEST_ISO3_CODE = "MDG";

    private static final String TEST_NUMERIC_CODE = "450";

    private static final AtomicInteger
            ORGANIZATION_SEQUENCE =
                    new AtomicInteger(30000);

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private OrganizationJpaRepository organizationJpaRepository;

    @Autowired
    private PersonJpaRepository personJpaRepository;

    @Autowired
    private DataCollectionJpaRepository dataCollectionJpaRepository;


    @Autowired
    private CountryJpaRepository countryJpaRepository;

    private CountryId countryId;

    @BeforeEach
    void setUp() {

        dataCollectionJpaRepository.deleteAllInBatch();

        personJpaRepository.deleteAllInBatch();

        organizationJpaRepository.deleteAllInBatch();

        CountryEntity country =
                countryJpaRepository.findByIso2Code(
                        TEST_ISO2_CODE)
                        .orElseGet(() ->
                                countryJpaRepository.save(
                                        new CountryEntity(
                                                UUID.randomUUID(),
                                                TEST_ISO2_CODE,
                                                TEST_ISO3_CODE,
                                                TEST_NUMERIC_CODE,
                                                "Madagascar",
                                                "Republic of Madagascar",
                                                true)));

        countryId =
                CountryId.of(country.getId());
    }
    @AfterEach
    void cleanUp() {

        dataCollectionJpaRepository.deleteAllInBatch();

        personJpaRepository.deleteAllInBatch();

        organizationJpaRepository.deleteAllInBatch();
    }
    @Test
    void shouldSaveAndFindById() {

        String code =
                nextOrganizationCode("AFDB");

        Organization saved =
                organizationRepository.save(
                        createOrganization(
                                code,
                                "African Development Bank",
                                OrganizationType.GOVERNMENT_AGENCY,
                                true));

        Optional<Organization> result =
                organizationRepository.findById(
                        saved.getOrganizationId());

        assertThat(result)
                .isPresent()
                .hasValueSatisfying(found -> {

                    assertThat(found.getOrganizationId())
                            .isEqualTo(
                                    saved.getOrganizationId());

                    assertThat(found.getCode().getValue())
                            .isEqualTo(code);

                    assertThat(found.getName().getValue())
                            .isEqualTo(
                                    "African Development Bank");

                    assertThat(found.getType())
                            .isEqualTo(
                                    OrganizationType.GOVERNMENT_AGENCY);

                    assertThat(found.getCountryId())
                            .isEqualTo(countryId);

                    assertThat(found.isActive())
                            .isTrue();
                });
    }

    @Test
    void shouldFindByCode() {

        String code =
                nextOrganizationCode("AFDB");

        Organization organization =
                organizationRepository.save(
                        createOrganization(
                                code,
                                "African Development Bank",
                                OrganizationType.GOVERNMENT_AGENCY,
                                true));

        Optional<Organization> result =
                organizationRepository.findByCode(
                        OrganizationCode.of(code));

        assertThat(result)
                .isPresent()
                .hasValueSatisfying(found ->
                        assertThat(found.getOrganizationId())
                                .isEqualTo(
                                        organization
                                                .getOrganizationId()));
    }

    @Test
    void shouldFindByCountryId() {

        String code =
                nextOrganizationCode("AFDB");

        Organization organization =
                organizationRepository.save(
                        createOrganization(
                                code,
                                "African Development Bank",
                                OrganizationType.GOVERNMENT_AGENCY,
                                true));

        List<Organization> result =
                organizationRepository.findByCountryId(
                        countryId);

        assertThat(result)
                .hasSize(1)
                .extracting(
                        Organization::getOrganizationId)
                .containsExactly(
                        organization.getOrganizationId());
    }

    @Test
    void shouldFindByType() {

        String code =
                nextOrganizationCode("AFDB");

        Organization organization =
                organizationRepository.save(
                        createOrganization(
                                code,
                                "African Development Bank",
                                OrganizationType.GOVERNMENT_AGENCY,
                                true));

        List<Organization> result =
                organizationRepository.findByType(
                        OrganizationType.GOVERNMENT_AGENCY);

        assertThat(result)
                .hasSize(1)
                .extracting(
                        Organization::getOrganizationId)
                .containsExactly(
                        organization.getOrganizationId());
    }

    @Test
    void shouldFindAllOrganizations() {

        String firstCode =
                nextOrganizationCode("AFDB");

        String secondCode =
                nextOrganizationCode("COMESA");

        organizationRepository.save(
                createOrganization(
                        firstCode,
                        "African Development Bank",
                        OrganizationType.GOVERNMENT_AGENCY,
                        true));

        organizationRepository.save(
                createOrganization(
                        secondCode,
                        "Common Market for Eastern and Southern Africa",
                        OrganizationType.MINISTRY,
                        true));

        List<Organization> result =
                organizationRepository.findAll();

        assertThat(result)
                .hasSize(2)
                .extracting(
                        organization ->
                                organization.getCode()
                                        .getValue())
                .containsExactlyInAnyOrder(
                        firstCode,
                        secondCode);
    }

    @Test
    void shouldFindOnlyActiveOrganizations() {

        String activeCode =
                nextOrganizationCode("AFDB");

        String inactiveCode =
                nextOrganizationCode("COMESA");

        Organization activeOrganization =
                organizationRepository.save(
                        createOrganization(
                                activeCode,
                                "African Development Bank",
                                OrganizationType.GOVERNMENT_AGENCY,
                                true));

        organizationRepository.save(
                createOrganization(
                        inactiveCode,
                        "Common Market for Eastern and Southern Africa",
                        OrganizationType.MINISTRY,
                        false));

        List<Organization> result =
                organizationRepository.findActive();

        assertThat(result)
                .hasSize(1)
                .extracting(
                        Organization::getOrganizationId)
                .containsExactly(
                        activeOrganization.getOrganizationId());
    }

    @Test
    void shouldCheckExistenceById() {

        String code =
                nextOrganizationCode("AFDB");

        Organization organization =
                organizationRepository.save(
                        createOrganization(
                                code,
                                "African Development Bank",
                                OrganizationType.GOVERNMENT_AGENCY,
                                true));

        assertThat(
                organizationRepository.existsById(
                        organization.getOrganizationId()))
                .isTrue();

        assertThat(
                organizationRepository.existsById(
                        OrganizationId.generate()))
                .isFalse();
    }

    @Test
    void shouldCheckExistenceByCode() {

        String code =
                nextOrganizationCode("AFDB");

        organizationRepository.save(
                createOrganization(
                        code,
                        "African Development Bank",
                        OrganizationType.GOVERNMENT_AGENCY,
                        true));

        assertThat(
                organizationRepository.existsByCode(
                        OrganizationCode.of(code)))
                .isTrue();

        String unknownCode =
                nextOrganizationCode("COMESA");

        assertThat(
                organizationRepository.existsByCode(
                        OrganizationCode.of(unknownCode)))
                .isFalse();
    }

    @Test
    void shouldDeleteOrganization() {

        String code =
                nextOrganizationCode("AFDB");

        Organization organization =
                organizationRepository.save(
                        createOrganization(
                                code,
                                "African Development Bank",
                                OrganizationType.GOVERNMENT_AGENCY,
                                true));

        assertThat(
                organizationRepository.existsById(
                        organization.getOrganizationId()))
                .isTrue();

        organizationRepository.delete(organization);

        assertThat(
                organizationRepository.existsById(
                        organization.getOrganizationId()))
                .isFalse();

        assertThat(
                organizationRepository.findById(
                        organization.getOrganizationId()))
                .isEmpty();
    }

    private String nextOrganizationCode(
            String prefix) {

        return prefix
                + ORGANIZATION_SEQUENCE
                        .incrementAndGet();
    }

    private Organization createOrganization(
            String code,
            String name,
            OrganizationType type,
            boolean active) {

        return Organization.restore(
                OrganizationId.generate(),
                OrganizationCode.of(code),
                OrganizationName.of(name),
                type,
                countryId,
                active);
    }
}
