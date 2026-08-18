package org.afdb.aikp.modules.iam.role.domain.valueobject;

import java.util.Objects;

/**
 * Value object representing the name of an IAM role.
 */
public final class RoleName {

    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 100;

    private final String value;

    private RoleName(String value) {
        this.value = value;
    }

    public static RoleName of(String value) {

        Objects.requireNonNull(
                value,
                "Role name cannot be null");

        String normalized =
                value.trim().replaceAll("\\s+", " ");

        if (normalized.isEmpty()) {
            throw new IllegalArgumentException(
                    "Role name cannot be empty");
        }

        if (normalized.length() < MIN_LENGTH) {
            throw new IllegalArgumentException(
                    "Role name must contain at least "
                            + MIN_LENGTH
                            + " characters");
        }

        if (normalized.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "Role name must contain at most "
                            + MAX_LENGTH
                            + " characters");
        }

        return new RoleName(normalized);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {

        return this == o
                || (o instanceof RoleName other
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