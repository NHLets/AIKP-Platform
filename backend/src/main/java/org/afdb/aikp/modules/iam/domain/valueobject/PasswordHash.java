package org.afdb.aikp.modules.iam.domain.valueobject;

import java.util.Objects;

public final class PasswordHash {

    private static final int MIN_LENGTH = 60;
    private static final int MAX_LENGTH = 255;

    private final String value;

    private PasswordHash(String value) {
        this.value = value;
    }

    public static PasswordHash of(String value) {
        Objects.requireNonNull(value, "Password hash cannot be null");

        String normalized = value.trim();

        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("Password hash cannot be empty");
        }

        if (normalized.length() < MIN_LENGTH) {
            throw new IllegalArgumentException("Invalid password hash");
        }

        if (normalized.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Password hash is too long");
        }

        return new PasswordHash(normalized);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PasswordHash other)) {
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
        return "********";
    }
}