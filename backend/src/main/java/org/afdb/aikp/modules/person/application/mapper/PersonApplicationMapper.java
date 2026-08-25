package org.afdb.aikp.modules.person.application.mapper;

import org.afdb.aikp.modules.person.application.response.PersonResponse;
import org.afdb.aikp.modules.person.application.response.PersonSummary;
import org.afdb.aikp.modules.person.domain.model.Person;

import org.springframework.stereotype.Component;

/**
 * Maps Person domain objects to application responses.
 */
@Component
public class PersonApplicationMapper {

    public PersonResponse toResponse(Person person) {

        return new PersonResponse(
                person.getPersonId().getValue(),
                person.getFullName().getValue(),
                person.getOrganizationId().getValue(),
                person.isActive());
    }

    public PersonSummary toSummary(Person person) {

        return new PersonSummary(
                person.getPersonId().getValue(),
                person.getFullName().getValue(),
                person.getOrganizationId().getValue(),
                person.isActive());
    }
}
