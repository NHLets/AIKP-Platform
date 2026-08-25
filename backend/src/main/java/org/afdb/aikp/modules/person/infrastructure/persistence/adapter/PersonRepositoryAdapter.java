package org.afdb.aikp.modules.person.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationId;

import org.afdb.aikp.modules.person.domain.model.Person;
import org.afdb.aikp.modules.person.domain.repository.PersonRepository;
import org.afdb.aikp.modules.person.domain.valueobject.PersonId;

import org.afdb.aikp.modules.person.infrastructure.persistence.entity.PersonEntity;
import org.afdb.aikp.modules.person.infrastructure.persistence.mapper.PersonPersistenceMapper;
import org.afdb.aikp.modules.person.infrastructure.persistence.repository.PersonJpaRepository;

import org.springframework.stereotype.Repository;

/**
 * Persistence adapter implementing the PersonRepository
 * domain abstraction.
 */
@Repository
public class PersonRepositoryAdapter
        implements PersonRepository {

    private final PersonJpaRepository personJpaRepository;

    private final PersonPersistenceMapper mapper;

    public PersonRepositoryAdapter(
            PersonJpaRepository personJpaRepository,
            PersonPersistenceMapper mapper) {

        this.personJpaRepository = personJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Person save(Person person) {

        PersonEntity entity = mapper.toEntity(person);

        PersonEntity savedEntity =
                personJpaRepository.save(entity);

        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Person> findById(PersonId id) {

        return personJpaRepository
                .findById(id.getValue())
                .map(mapper::toDomain);
    }

    @Override
    public List<Person> findAll() {

        return personJpaRepository
                .findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Person> findByOrganizationId(
            OrganizationId organizationId) {

        return personJpaRepository
                .findByOrganizationId(
                        organizationId.getValue())
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Person> findByActiveTrue() {

        return personJpaRepository
                .findByActiveTrue()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsById(PersonId id) {

        return personJpaRepository
                .existsById(id.getValue());
    }

    @Override
    public void delete(Person person) {

        personJpaRepository.deleteById(
                person.getPersonId().getValue());
    }
}
