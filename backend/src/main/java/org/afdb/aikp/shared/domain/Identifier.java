package org.afdb.aikp.shared.domain;

import java.io.Serializable;
import java.util.Objects;

/**
 * Base class for strongly typed identifiers.
 *
 * @param <T> underlying identifier type
 */
public abstract class Identifier<T extends Serializable> implements Serializable {

    private final T value;

    protected Identifier(T value) {
        this.value = Objects.requireNonNull(value, "Identifier value cannot be null.");
    }

    public T getValue() {
        return value;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Identifier<?> that = (Identifier<?>) o;
        return value.equals(that.value);
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