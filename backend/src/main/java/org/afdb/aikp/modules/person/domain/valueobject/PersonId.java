package org.afdb.aikp.modules.person.domain.valueobject;

import java.util.UUID;

import org.afdb.aikp.shared.domain.Identifier;

/**
 * Strongly typed identifier for Person.
 */
public final class PersonId extends Identifier<UUID> {

    private PersonId(UUID value) {
        super(value);
    }

    /**
     * Creates a Person identifier from an existing UUID.
     */
    public static PersonId of(UUID value) {
        return new PersonId(value);
    }

    /**
     * Generates a new Person identifier.
     */
    public static PersonId generate() {
        return new PersonId(UUID.randomUUID());
    }
}
