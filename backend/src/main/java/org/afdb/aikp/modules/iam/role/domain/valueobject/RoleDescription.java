package org.afdb.aikp.modules.iam.role.domain.valueobject;

import java.util.Objects;

/**
 * Value object representing the description of an IAM role.
 */
public final class RoleDescription {

    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 500;

    private final String value;

    private RoleDescription(String value) {
        this.value = value;
    }

    public static RoleDescription of(String value) {

        Objects.requireNonNull(
                value,
                "Role description cannot be null");

        String normalized =
                value.trim().replaceAll("\\s+", " ");

        if (normalized.isEmpty()) {
            throw new IllegalArgumentException(
                    "Role description cannot be empty");
        }

        if (normalized.length() < MIN_LENGTH) {
            throw new IllegalArgumentException(
                    "Role description must contain at least "
                            + MIN_LENGTH
                            + " characters");
        }

        if (normalized.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "Role description must contain at most "
                            + MAX_LENGTH
                            + " characters");
        }

        return new RoleDescription(normalized);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {

        return this == o
                || (o instanceof RoleDescription other
                && value.equals(other.value));
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