package org.afdb.aikp.modules.person.domain.exception;

import org.afdb.aikp.modules.person.domain.valueobject.PersonId;
import org.afdb.aikp.shared.exception.NotFoundException;

/**
 * Thrown when a Person cannot be found.
 */
public class PersonNotFoundException
        extends NotFoundException {

    public PersonNotFoundException(PersonId id) {
        super("Person not found with id: "
                + id.getValue());
    }
}
