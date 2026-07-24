package org.afdb.aikp.shared.domain;

import java.util.Objects;

/**
 * Base class for all domain entities.
 *
 * @param <ID> identifier type
 */
public abstract class Entity<ID extends Identifier<?>> {

    private final ID id;

    protected Entity(ID id) {
        this.id = Objects.requireNonNull(id);
    }

    public final ID getId() {
        return id;
    }

    @Override
    public final boolean equals(Object object) {

        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        Entity<?> other = (Entity<?>) object;

        return id.equals(other.id);
    }

    @Override
    public final int hashCode() {
        return id.hashCode();
    }

}