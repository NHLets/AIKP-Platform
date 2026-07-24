package org.afdb.aikp.shared.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.Hibernate;

import java.util.Objects;
import java.util.UUID;

/**
 * Base class for all JPA entities.
 *
 * <p>
 * Provides:
 * <ul>
 *     <li>UUID primary key</li>
 *     <li>Entity identity semantics</li>
 *     <li>Hibernate proxy-safe equals/hashCode</li>
 * </ul>
 * </p>
 */
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @Column(
            name = "id",
            nullable = false,
            updatable = false
    )
    private UUID id;

    /**
     * Required by JPA.
     */
    protected BaseEntity() {
    }

    /**
     * Creates a new entity.
     *
     * @param id entity identifier
     */
    protected BaseEntity(UUID id) {
        this.id = Objects.requireNonNull(id, "Entity id cannot be null.");
    }

    /**
     * Returns the entity identifier.
     */
    public UUID getId() {
        return id;
    }

    /**
     * Used only by JPA and subclasses.
     */
    protected void setId(UUID id) {
        this.id = Objects.requireNonNull(id);
    }

    @Override
    public final boolean equals(Object object) {

        if (this == object) {
            return true;
        }

        if (object == null) {
            return false;
        }

        if (Hibernate.getClass(this) != Hibernate.getClass(object)) {
            return false;
        }

        BaseEntity other = (BaseEntity) object;

        return id != null && id.equals(other.id);
    }

    @Override
    public final int hashCode() {
        return Hibernate.getClass(this).hashCode();
    }

}