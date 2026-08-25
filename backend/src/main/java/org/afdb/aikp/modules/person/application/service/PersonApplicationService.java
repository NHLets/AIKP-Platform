package org.afdb.aikp.modules.person.application.service;

import java.util.List;
import java.util.Objects;

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

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Application service for managing Persons.
 */
@Service
@Transactional
public class PersonApplicationService {

    private final PersonRepository personRepository;

    private final OrganizationRepository organizationRepository;

    private final PersonApplicationMapper mapper;

    public PersonApplicationService(
            PersonRepository personRepository,
            OrganizationRepository organizationRepository,
            PersonApplicationMapper mapper) {

        this.personRepository = Objects.requireNonNull(
                personRepository,
                "PersonRepository cannot be null.");

        this.organizationRepository = Objects.requireNonNull(
                organizationRepository,
                "OrganizationRepository cannot be null.");

        this.mapper = Objects.requireNonNull(
                mapper,
                "PersonApplicationMapper cannot be null.");
    }

    /**
     * Creates a new Person.
     */
    public PersonResponse createPerson(
            CreatePersonCommand command) {

        Objects.requireNonNull(
                command,
                "CreatePersonCommand cannot be null.");

        OrganizationId organizationId =
                OrganizationId.of(command.organizationId());

        ensureOrganizationExists(organizationId);

        Person person = Person.create(
                PersonId.generate(),
                PersonFullName.of(command.fullName()),
                organizationId);

        Person savedPerson =
                personRepository.save(person);

        return mapper.toResponse(savedPerson);
    }

    /**
     * Updates an existing Person.
     */
    public PersonResponse updatePerson(
            UpdatePersonCommand command) {

        Objects.requireNonNull(
                command,
                "UpdatePersonCommand cannot be null.");

        PersonId personId =
                PersonId.of(command.personId());

        Person person = findPersonOrThrow(personId);

        OrganizationId organizationId =
                OrganizationId.of(command.organizationId());

        ensureOrganizationExists(organizationId);

        person.changeFullName(
                PersonFullName.of(command.fullName()));

        person.changeOrganization(organizationId);

        Person savedPerson =
                personRepository.save(person);

        return mapper.toResponse(savedPerson);
    }

    /**
     * Activates a Person.
     */
    public PersonResponse activatePerson(
            ActivatePersonCommand command) {

        Objects.requireNonNull(
                command,
                "ActivatePersonCommand cannot be null.");

        PersonId personId =
                PersonId.of(command.personId());

        Person person = findPersonOrThrow(personId);

        person.activate();

        Person savedPerson =
                personRepository.save(person);

        return mapper.toResponse(savedPerson);
    }

    /**
     * Deactivates a Person.
     */
    public PersonResponse deactivatePerson(
            DeactivatePersonCommand command) {

        Objects.requireNonNull(
                command,
                "DeactivatePersonCommand cannot be null.");

        PersonId personId =
                PersonId.of(command.personId());

        Person person = findPersonOrThrow(personId);

        person.deactivate();

        Person savedPerson =
                personRepository.save(person);

        return mapper.toResponse(savedPerson);
    }

    /**
     * Deletes a Person.
     */
    public void deletePerson(
            DeletePersonCommand command) {

        Objects.requireNonNull(
                command,
                "DeletePersonCommand cannot be null.");

        PersonId personId =
                PersonId.of(command.personId());

        Person person = findPersonOrThrow(personId);

        personRepository.delete(person);
    }

    /**
     * Retrieves a Person by identifier.
     */
    @Transactional(readOnly = true)
    public PersonResponse getPerson(
            GetPersonQuery query) {

        Objects.requireNonNull(
                query,
                "GetPersonQuery cannot be null.");

        PersonId personId =
                PersonId.of(query.personId());

        Person person =
                findPersonOrThrow(personId);

        return mapper.toResponse(person);
    }

    /**
     * Retrieves all Persons.
     */
    @Transactional(readOnly = true)
    public List<PersonSummary> getPersons(
            GetPersonsQuery query) {

        Objects.requireNonNull(
                query,
                "GetPersonsQuery cannot be null.");

        return personRepository.findAll()
                .stream()
                .map(mapper::toSummary)
                .toList();
    }

    /**
     * Retrieves Persons associated with an Organization.
     */
    @Transactional(readOnly = true)
    public List<PersonSummary> getPersonsByOrganization(
            GetPersonsByOrganizationQuery query) {

        Objects.requireNonNull(
                query,
                "GetPersonsByOrganizationQuery cannot be null.");

        OrganizationId organizationId =
                OrganizationId.of(query.organizationId());

        ensureOrganizationExists(organizationId);

        return personRepository
                .findByOrganizationId(organizationId)
                .stream()
                .map(mapper::toSummary)
                .toList();
    }

    /**
     * Retrieves all active Persons.
     */
    @Transactional(readOnly = true)
    public List<PersonSummary> getActivePersons(
            GetActivePersonsQuery query) {

        Objects.requireNonNull(
                query,
                "GetActivePersonsQuery cannot be null.");

        return personRepository.findByActiveTrue()
                .stream()
                .map(mapper::toSummary)
                .toList();
    }

    private Person findPersonOrThrow(
            PersonId personId) {

        return personRepository
                .findById(personId)
                .orElseThrow(() ->
                        new PersonNotFoundException(personId));
    }

    private void ensureOrganizationExists(
            OrganizationId organizationId) {

        if (!organizationRepository
                .existsById(organizationId)) {

            throw new OrganizationNotFoundException(
                    organizationId);
        }
    }
}
