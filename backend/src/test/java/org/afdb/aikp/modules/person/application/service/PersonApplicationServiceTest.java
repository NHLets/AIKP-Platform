package org.afdb.aikp.modules.person.application.service;

import org.afdb.aikp.modules.organization.domain.exception.OrganizationNotFoundException;
import org.afdb.aikp.modules.organization.domain.repository.OrganizationRepository;
import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationId;
import org.afdb.aikp.modules.person.application.command.ActivatePersonCommand;
import org.afdb.aikp.modules.person.application.command.CreatePersonCommand;
import org.afdb.aikp.modules.person.application.command.DeactivatePersonCommand;
import org.afdb.aikp.modules.person.application.command.DeletePersonCommand;
import org.afdb.aikp.modules.person.application.command.UpdatePersonCommand;
import org.afdb.aikp.modules.person.application.mapper.PersonApplicationMapper;
import org.afdb.aikp.modules.person.application.query.GetActivePersonsQuery;
import org.afdb.aikp.modules.person.application.query.GetPersonQuery;
import org.afdb.aikp.modules.person.application.query.GetPersonsByOrganizationQuery;
import org.afdb.aikp.modules.person.application.query.GetPersonsQuery;
import org.afdb.aikp.modules.person.application.response.PersonResponse;
import org.afdb.aikp.modules.person.application.response.PersonSummary;
import org.afdb.aikp.modules.person.domain.exception.PersonNotFoundException;
import org.afdb.aikp.modules.person.domain.model.Person;
import org.afdb.aikp.modules.person.domain.repository.PersonRepository;
import org.afdb.aikp.modules.person.domain.valueobject.PersonFullName;
import org.afdb.aikp.modules.person.domain.valueobject.PersonId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PersonApplicationServiceTest {

    private PersonRepository personRepository;
    private OrganizationRepository organizationRepository;
    private PersonApplicationService service;

    @BeforeEach
    void setUp() {

        personRepository = mock(PersonRepository.class);
        organizationRepository = mock(OrganizationRepository.class);

        service = new PersonApplicationService(
                personRepository,
                organizationRepository,
                new PersonApplicationMapper());
    }

    @Test
    void shouldCreatePerson() {

        UUID organizationUuid = UUID.randomUUID();

        when(organizationRepository.existsById(
                OrganizationId.of(organizationUuid)))
                .thenReturn(true);

        when(personRepository.save(any(Person.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PersonResponse response =
                service.createPerson(
                        new CreatePersonCommand(
                                "John Doe",
                                organizationUuid));

        assertThat(response).isNotNull();
        assertThat(response.fullName()).isEqualTo("John Doe");
        assertThat(response.organizationId())
                .isEqualTo(organizationUuid);

        verify(personRepository).save(any(Person.class));
    }

    @Test
    void shouldThrowExceptionWhenCreatingPersonWithUnknownOrganization() {

        UUID organizationUuid = UUID.randomUUID();

        when(organizationRepository.existsById(
                OrganizationId.of(organizationUuid)))
                .thenReturn(false);

        CreatePersonCommand command =
                new CreatePersonCommand(
                        "John Doe",
                        organizationUuid);

        assertThatThrownBy(
                () -> service.createPerson(command))
                .isInstanceOf(OrganizationNotFoundException.class);

        verify(personRepository, never()).save(any());
    }

    @Test
    void shouldGetPerson() {

        UUID personUuid = UUID.randomUUID();
        UUID organizationUuid = UUID.randomUUID();

        Person person =
                Person.create(
                        PersonId.of(personUuid),
                        PersonFullName.of("John Doe"),
                        OrganizationId.of(organizationUuid));

        when(personRepository.findById(
                PersonId.of(personUuid)))
                .thenReturn(Optional.of(person));

        PersonResponse response =
                service.getPerson(
                        new GetPersonQuery(personUuid));

        assertThat(response.id()).isEqualTo(personUuid);
        assertThat(response.fullName()).isEqualTo("John Doe");
        assertThat(response.organizationId())
                .isEqualTo(organizationUuid);
        assertThat(response.active()).isTrue();
    }

    @Test
    void shouldThrowExceptionWhenPersonDoesNotExist() {

        UUID personUuid = UUID.randomUUID();

        when(personRepository.findById(any(PersonId.class)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> service.getPerson(
                        new GetPersonQuery(personUuid)))
                .isInstanceOf(PersonNotFoundException.class);
    }

    @Test
    void shouldGetAllPersons() {

        Person first =
                Person.create(
                        PersonId.of(UUID.randomUUID()),
                        PersonFullName.of("John Doe"),
                        OrganizationId.of(UUID.randomUUID()));

        Person second =
                Person.create(
                        PersonId.of(UUID.randomUUID()),
                        PersonFullName.of("Jane Doe"),
                        OrganizationId.of(UUID.randomUUID()));

        when(personRepository.findAll())
                .thenReturn(List.of(first, second));

        List<PersonSummary> persons =
                service.getPersons(
                        new GetPersonsQuery());

        assertThat(persons)
                .hasSize(2)
                .extracting(PersonSummary::fullName)
                .containsExactly("John Doe", "Jane Doe");
    }

    @Test
    void shouldGetPersonsByOrganization() {

        UUID organizationUuid = UUID.randomUUID();

        Person person =
                Person.create(
                        PersonId.of(UUID.randomUUID()),
                        PersonFullName.of("John Doe"),
                        OrganizationId.of(organizationUuid));

        when(organizationRepository.existsById(
                OrganizationId.of(organizationUuid)))
                .thenReturn(true);

        when(personRepository.findByOrganizationId(
                OrganizationId.of(organizationUuid)))
                .thenReturn(List.of(person));

        List<PersonSummary> persons =
                service.getPersonsByOrganization(
                        new GetPersonsByOrganizationQuery(
                                organizationUuid));

        assertThat(persons).hasSize(1);
        assertThat(persons.getFirst().organizationId())
                .isEqualTo(organizationUuid);
    }

    @Test
    void shouldGetActivePersons() {

        Person active =
                Person.create(
                        PersonId.of(UUID.randomUUID()),
                        PersonFullName.of("John Doe"),
                        OrganizationId.of(UUID.randomUUID()));

        when(personRepository.findByActiveTrue())
                .thenReturn(List.of(active));

        List<PersonSummary> persons =
                service.getActivePersons(
                        new GetActivePersonsQuery());

        assertThat(persons).hasSize(1);
        assertThat(persons.getFirst().active()).isTrue();
    }

    @Test
    void shouldUpdatePerson() {

        UUID personUuid = UUID.randomUUID();
        UUID organizationUuid = UUID.randomUUID();
        UUID newOrganizationUuid = UUID.randomUUID();

        Person person =
                Person.create(
                        PersonId.of(personUuid),
                        PersonFullName.of("John Doe"),
                        OrganizationId.of(organizationUuid));

        when(personRepository.findById(
                PersonId.of(personUuid)))
                .thenReturn(Optional.of(person));

        when(organizationRepository.existsById(
                OrganizationId.of(newOrganizationUuid)))
                .thenReturn(true);

        when(personRepository.save(any(Person.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PersonResponse response =
                service.updatePerson(
                        new UpdatePersonCommand(
                                personUuid,
                                "Jane Doe",
                                newOrganizationUuid));

        assertThat(response.fullName()).isEqualTo("Jane Doe");
        assertThat(response.organizationId())
                .isEqualTo(newOrganizationUuid);

        verify(personRepository).save(person);
    }

    @Test
    void shouldActivatePerson() {

        UUID personUuid = UUID.randomUUID();

        Person person =
                Person.create(
                        PersonId.of(personUuid),
                        PersonFullName.of("John Doe"),
                        OrganizationId.of(UUID.randomUUID()));

        person.deactivate();

        when(personRepository.findById(any(PersonId.class)))
                .thenReturn(Optional.of(person));

        when(personRepository.save(any(Person.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PersonResponse response =
                service.activatePerson(
                        new ActivatePersonCommand(personUuid));

        assertThat(response.active()).isTrue();

        verify(personRepository).save(person);
    }

    @Test
    void shouldDeactivatePerson() {

        UUID personUuid = UUID.randomUUID();

        Person person =
                Person.create(
                        PersonId.of(personUuid),
                        PersonFullName.of("John Doe"),
                        OrganizationId.of(UUID.randomUUID()));

        when(personRepository.findById(any(PersonId.class)))
                .thenReturn(Optional.of(person));

        when(personRepository.save(any(Person.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PersonResponse response =
                service.deactivatePerson(
                        new DeactivatePersonCommand(personUuid));

        assertThat(response.active()).isFalse();

        verify(personRepository).save(person);
    }

    @Test
    void shouldDeletePerson() {

        UUID personUuid = UUID.randomUUID();

        Person person =
                Person.create(
                        PersonId.of(personUuid),
                        PersonFullName.of("John Doe"),
                        OrganizationId.of(UUID.randomUUID()));

        when(personRepository.findById(any(PersonId.class)))
                .thenReturn(Optional.of(person));

        service.deletePerson(
                new DeletePersonCommand(personUuid));

        verify(personRepository).delete(person);
    }

    @Test
    void shouldThrowExceptionWhenDeletingUnknownPerson() {

        UUID personUuid = UUID.randomUUID();

        when(personRepository.findById(any(PersonId.class)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> service.deletePerson(
                        new DeletePersonCommand(personUuid)))
                .isInstanceOf(PersonNotFoundException.class);

        verify(personRepository, never()).delete(any());
    }
}
