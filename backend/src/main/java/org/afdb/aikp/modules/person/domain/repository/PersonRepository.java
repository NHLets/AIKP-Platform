package org.afdb.aikp.modules.person.domain.repository;

import java.util.List;
import java.util.Optional;

import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationId;
import org.afdb.aikp.modules.person.domain.model.Person;
import org.afdb.aikp.modules.person.domain.valueobject.PersonId;

/**
 * Repository abstraction for Person aggregates.
 */
public interface PersonRepository {

    Person save(Person person);

    Optional<Person> findById(PersonId id);

    List<Person> findAll();

    List<Person> findByOrganizationId(
            OrganizationId organizationId);

    List<Person> findByActiveTrue();

    boolean existsById(PersonId id);

    void delete(Person person);
}
