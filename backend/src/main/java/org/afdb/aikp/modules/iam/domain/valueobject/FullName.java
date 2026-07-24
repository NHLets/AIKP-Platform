package org.afdb.aikp.modules.iam.domain.valueobject;

import java.util.Objects;

public final class FullName {

    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 150;

    private final String value;

    private FullName(String value) {
        this.value = value;
    }

    public static FullName of(String value) {
        Objects.requireNonNull(value, "Full name cannot be null");

        String normalized = value.trim().replaceAll("\\s+", " ");

        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be empty");
        }

        if (normalized.length() < MIN_LENGTH) {
            throw new IllegalArgumentException(
                    "Full name must contain at least " + MIN_LENGTH + " characters");
        }

        if (normalized.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "Full name must contain at most " + MAX_LENGTH + " characters");
        }

        return new FullName(normalized);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        return this == o
                || (o instanceof FullName other && value.equals(other.value));
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