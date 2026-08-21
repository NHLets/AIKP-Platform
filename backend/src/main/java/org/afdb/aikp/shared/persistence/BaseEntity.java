package org.afdb.aikp.shared.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;

import org.hibernate.Hibernate;
import org.springframework.data.domain.Persistable;

import java.util.Objects;
import java.util.UUID;
import jakarta.persistence.PostLoad;

/**
 * Base class for all JPA entities.
 *
 * <p>
 * Provides:
 * <ul>
 *     <li>UUID primary key</li>
 *     <li>Explicit new/existing entity detection for Spring Data JPA</li>
 *     <li>Entity identity semantics</li>
 *     <li>Hibernate proxy-safe equals/hashCode</li>
 * </ul>
 * </p>
 */
@MappedSuperclass
public abstract class BaseEntity
        implements Persistable<UUID> {

    @Id
    @Column(
            name = "id",
            nullable = false,
            updatable = false
    )
    private UUID id;

    /**
     * Indicates whether this entity has already been persisted.
     *
     * <p>
     * This flag is transient and exists only to guide Spring Data JPA
     * when deciding between persist() and merge().
     * </p>
     */
    @Transient
    private boolean isNew = true;

    /**
     * Required by JPA.
     */
    protected BaseEntity() {
    }

    /**
     * Creates a new entity with an application-assigned UUID.
     *
     * @param id entity identifier
     */
    protected BaseEntity(UUID id) {
        this.id = Objects.requireNonNull(
                id,
                "Entity id cannot be null.");
    }

    /**
     * Returns the entity identifier.
     */
    @Override
    public UUID getId() {
        return id;
    }

    /**
     * Explicitly tells Spring Data JPA whether this entity is new.
     *
     * <p>
     * AIKP domain aggregates generate their UUID identifiers before
     * persistence, so checking whether the ID is null is insufficient.
     * </p>
     */
    @Override
    public boolean isNew() {
        return isNew;
    }

    /**
     * Marks the entity as already persisted.
     *
     * This method is used after loading an existing entity from JPA.
     */
    protected void markNotNew() {
        this.isNew = false;
    }

    /**
 * Marks entities loaded from the database as existing.
 */
    @PostLoad
    protected void onLoad() {
    this.isNew = false;
    }
    /**
     * Marks the entity as new.
     */
    protected void markNew() {
        this.isNew = true;
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

        if (Hibernate.getClass(this)
                != Hibernate.getClass(object)) {
            return false;
        }

        BaseEntity other = (BaseEntity) object;

        return id != null
                && id.equals(other.id);
    }

    @Override
    public final int hashCode() {
        return Hibernate.getClass(this).hashCode();
    }
}