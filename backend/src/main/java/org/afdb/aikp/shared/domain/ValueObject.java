package org.afdb.aikp.shared.domain;

import java.io.Serializable;
import java.util.Objects;

/**
 * Base class for immutable value objects.
 *
 * @param <T> underlying value type
 */
public abstract class ValueObject<T> implements Serializable {

    private final T value;

    protected ValueObject(T value) {
        this.value = Objects.requireNonNull(value);
    }

    public final T getValue() {
        return value;
    }

    @Override
    public final boolean equals(Object object) {

        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        ValueObject<?> other = (ValueObject<?>) object;

        return value.equals(other.value);
    }

    @Override
    public final int hashCode() {
        return value.hashCode();
    }

    @Override
    public final String toString() {
        return value.toString();
    }

}