package org.afdb.aikp.modules.person.infrastructure.persistence.mapper;

import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationId;

import org.afdb.aikp.modules.person.domain.model.Person;
import org.afdb.aikp.modules.person.domain.valueobject.PersonFullName;
import org.afdb.aikp.modules.person.domain.valueobject.PersonId;

import org.afdb.aikp.modules.person.infrastructure.persistence.entity.PersonEntity;

import org.springframework.stereotype.Component;

/**
 * Maps Person domain objects to persistence entities
 * and persistence entities back to domain objects.
 */
@Component
public class PersonPersistenceMapper {

    public PersonEntity toEntity(Person person) {

        return new PersonEntity(
                person.getPersonId().getValue(),
                person.getFullName().getValue(),
                person.getOrganizationId().getValue(),
                person.isActive()
        );
    }

    public Person toDomain(PersonEntity entity) {

        return Person.restore(
                PersonId.of(entity.getId()),
                PersonFullName.of(entity.getFullName()),
                OrganizationId.of(entity.getOrganizationId()),
                entity.isActive()
        );
    }
}
