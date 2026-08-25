package org.afdb.aikp.modules.person.infrastructure.persistence.adapter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;

import org.afdb.aikp.modules.country.domain.model.Country;
import org.afdb.aikp.modules.country.domain.repository.CountryRepository;
import org.afdb.aikp.modules.country.domain.valueobject.CountryId;
import org.afdb.aikp.modules.country.domain.valueobject.CountryName;
import org.afdb.aikp.modules.country.domain.valueobject.Iso2Code;
import org.afdb.aikp.modules.country.domain.valueobject.Iso3Code;
import org.afdb.aikp.modules.country.domain.valueobject.NumericCode;
import org.afdb.aikp.modules.country.domain.valueobject.OfficialCountryName;

import org.afdb.aikp.modules.organization.domain.enums.OrganizationType;
import org.afdb.aikp.modules.organization.domain.model.Organization;
import org.afdb.aikp.modules.organization.domain.repository.OrganizationRepository;
import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationCode;
import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationId;
import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationName;

import org.afdb.aikp.modules.person.domain.model.Person;
import org.afdb.aikp.modules.person.domain.repository.PersonRepository;
import org.afdb.aikp.modules.person.domain.valueobject.PersonFullName;
import org.afdb.aikp.modules.person.domain.valueobject.PersonId;

import org.afdb.aikp.modules.person.infrastructure.persistence.repository.PersonJpaRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PersonRepositoryAdapterIntegrationTest {

    private static final String TEST_ISO2_CODE = "MG";
    private static final String TEST_ISO3_CODE = "MDG";
    private static final String TEST_NUMERIC_CODE = "450";

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonJpaRepository personJpaRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private CountryRepository countryRepository;

    @BeforeEach
    void setUp() {

        personJpaRepository.deleteAll();
    }

    @AfterEach
    void cleanUp() {

        personJpaRepository.deleteAll();
    }

    @Test
    void shouldSaveAndFindPersonById() {

        OrganizationId organizationId =
                createOrganization();

        Person person =
                createPerson(
                        "John Doe",
                        organizationId);

        Person savedPerson =
                personRepository.save(person);

        assertThat(savedPerson)
                .isNotNull();

        assertThat(
                personRepository.findById(
                        savedPerson.getPersonId()))
                .isPresent()
                .hasValueSatisfying(foundPerson -> {

                    assertThat(
                            foundPerson.getPersonId())
                            .isEqualTo(
                                    savedPerson.getPersonId());

                    assertThat(
                            foundPerson.getFullName()
                                    .getValue())
                            .isEqualTo(
                                    "John Doe");

                    assertThat(
                            foundPerson.getOrganizationId())
                            .isEqualTo(
                                    organizationId);

                    assertThat(
                            foundPerson.isActive())
                            .isTrue();
                });
    }

    @Test
    void shouldFindAllPersons() {

        OrganizationId organizationId =
                createOrganization();

        personRepository.save(
                createPerson(
                        "Alice Martin",
                        organizationId));

        personRepository.save(
                createPerson(
                        "Bob Johnson",
                        organizationId));

        List<Person> persons =
                personRepository.findAll();

        assertThat(persons)
                .hasSize(2);
    }

    @Test
    void shouldFindPersonsByOrganizationId() {

        OrganizationId organizationId =
                createOrganization();

        OrganizationId anotherOrganizationId =
                createOrganization();

        personRepository.save(
                createPerson(
                        "Alice Martin",
                        organizationId));

        personRepository.save(
                createPerson(
                        "Bob Johnson",
                        organizationId));

        personRepository.save(
                createPerson(
                        "Charlie Brown",
                        anotherOrganizationId));

        List<Person> persons =
                personRepository.findByOrganizationId(
                        organizationId);

        assertThat(persons)
                .hasSize(2)
                .allSatisfy(person ->
                        assertThat(
                                person.getOrganizationId())
                                .isEqualTo(
                                        organizationId));
    }

    @Test
    void shouldFindOnlyActivePersons() {

        OrganizationId organizationId =
                createOrganization();

        Person activePerson =
                createPerson(
                        "Active Person",
                        organizationId);

        Person inactivePerson =
                createPerson(
                        "Inactive Person",
                        organizationId);

        inactivePerson.deactivate();

        personRepository.save(
                activePerson);

        personRepository.save(
                inactivePerson);

        List<Person> activePersons =
                personRepository.findByActiveTrue();

        assertThat(activePersons)
                .hasSize(1);

        assertThat(
                activePersons.get(0)
                        .getPersonId())
                .isEqualTo(
                        activePerson.getPersonId());
    }

    @Test
    void shouldCheckPersonExistenceById() {

        OrganizationId organizationId =
                createOrganization();

        Person savedPerson =
                personRepository.save(
                        createPerson(
                                "John Doe",
                                organizationId));

        assertThat(
                personRepository.existsById(
                        savedPerson.getPersonId()))
                .isTrue();

        assertThat(
                personRepository.existsById(
                        PersonId.of(
                                UUID.randomUUID())))
                .isFalse();
    }

    @Test
    void shouldDeletePerson() {

        OrganizationId organizationId =
                createOrganization();

        Person savedPerson =
                personRepository.save(
                        createPerson(
                                "John Doe",
                                organizationId));

        personRepository.delete(
                savedPerson);

        assertThat(
                personRepository.findById(
                        savedPerson.getPersonId()))
                .isEmpty();
    }

    private OrganizationId createOrganization() {

        Country country =
                getOrCreateCountry();

        Organization organization =
                Organization.create(
                        OrganizationId.of(
                                UUID.randomUUID()),

                        OrganizationCode.of(
                                "ORG"
                                        + UUID.randomUUID()
                                                .toString()
                                                .replace("-", "")
                                                .substring(0, 10)
                                                .toUpperCase()),

                        OrganizationName.of(
                                "Test Organization "
                                        + UUID.randomUUID()),

                        OrganizationType.GOVERNMENT_AGENCY,

                        country.getId());

        Organization savedOrganization =
                organizationRepository.save(
                        organization);

        return savedOrganization.getId();
    }

    private Country getOrCreateCountry() {

        Iso2Code iso2Code =
                Iso2Code.of(
                        TEST_ISO2_CODE);

        return countryRepository
                .findByIso2Code(
                        iso2Code)
                .orElseGet(() -> {

                    Country country =
                            Country.create(
                                    CountryId.of(
                                            UUID.randomUUID()),

                                    Iso2Code.of(
                                            TEST_ISO2_CODE),

                                    Iso3Code.of(
                                            TEST_ISO3_CODE),

                                    NumericCode.of(
                                            TEST_NUMERIC_CODE),

                                    CountryName.of(
                                            "Madagascar"),

                                    OfficialCountryName.of(
                                            "Republic of Madagascar"));

                    return countryRepository.save(
                            country);
                });
    }

    private Person createPerson(
            String fullName,
            OrganizationId organizationId) {

        return Person.create(
                PersonId.of(
                        UUID.randomUUID()),

                PersonFullName.of(
                        fullName),

                organizationId);
    }
}
