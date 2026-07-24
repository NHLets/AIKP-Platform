package org.afdb.aikp.modules.iam.domain.valueobject;

import java.util.Objects;

public final class Username {

    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 50;

    private final String value;

    private Username(String value) {
        this.value = value;
    }

    public static Username of(String value) {
        Objects.requireNonNull(value, "Username cannot be null");

        String normalized = value.trim();

        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }

        if (normalized.length() < MIN_LENGTH) {
            throw new IllegalArgumentException(
                    "Username must contain at least " + MIN_LENGTH + " characters");
        }

        if (normalized.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "Username must contain at most " + MAX_LENGTH + " characters");
        }

        return new Username(normalized);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Username other)) {
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