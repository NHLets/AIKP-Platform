package org.afdb.aikp.modules.person.domain.valueobject;

import java.util.Objects;

/**
 * Value object representing the official full name of a Person.
 *
 * <p>
 * The name is stored as officially provided by the organization
 * responsible for the nomination of the person.
 * </p>
 */
public final class PersonFullName {

    private static final int MAX_LENGTH = 255;

    private final String value;

    private PersonFullName(String value) {

        this.value = validate(value);
    }

    public static PersonFullName of(String value) {
        return new PersonFullName(value);
    }

    private String validate(String value) {

        Objects.requireNonNull(
                value,
                "Person full name cannot be null.");

        String normalizedValue = value.trim();

        if (normalizedValue.isBlank()) {
            throw new IllegalArgumentException(
                    "Person full name cannot be blank.");
        }

        if (normalizedValue.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "Person full name cannot exceed "
                            + MAX_LENGTH
                            + " characters.");
        }

        return normalizedValue;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object object) {

        if (this == object) {
            return true;
        }

        if (!(object instanceof PersonFullName other)) {
            return false;
        }

        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }
}
