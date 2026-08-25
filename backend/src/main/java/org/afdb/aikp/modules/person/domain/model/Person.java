package org.afdb.aikp.modules.person.domain.model;

import java.util.Objects;

import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationId;
import org.afdb.aikp.modules.person.domain.valueobject.PersonFullName;
import org.afdb.aikp.modules.person.domain.valueobject.PersonId;
import org.afdb.aikp.shared.domain.AggregateRoot;

/**
 * Person Aggregate Root.
 *
 * <p>
 * Represents an individual officially associated with an organization
 * within the AIKP ecosystem.
 * </p>
 */
public final class Person
        extends AggregateRoot<PersonId> {

    private PersonFullName fullName;

    private OrganizationId organizationId;

    private boolean active;

    private Person(
            PersonId id,
            PersonFullName fullName,
            OrganizationId organizationId,
            boolean active) {

        super(Objects.requireNonNull(
                id,
                "Person ID cannot be null."));

        this.fullName = Objects.requireNonNull(
                fullName,
                "Person full name cannot be null.");

        this.organizationId = Objects.requireNonNull(
                organizationId,
                "Organization ID cannot be null.");

        this.active = active;
    }

    /**
     * Creates a new Person.
     */
    public static Person create(
            PersonId id,
            PersonFullName fullName,
            OrganizationId organizationId) {

        return new Person(
                id,
                fullName,
                organizationId,
                true);
    }

    /**
     * Restores an existing Person aggregate from persistence.
     */
    public static Person restore(
            PersonId id,
            PersonFullName fullName,
            OrganizationId organizationId,
            boolean active) {

        return new Person(
                id,
                fullName,
                organizationId,
                active);
    }

    /**
     * Changes the person's official full name.
     */
    public void changeFullName(
            PersonFullName fullName) {

        this.fullName = Objects.requireNonNull(
                fullName,
                "Person full name cannot be null.");
    }

    /**
     * Changes the organization associated with the person.
     */
    public void changeOrganization(
            OrganizationId organizationId) {

        this.organizationId = Objects.requireNonNull(
                organizationId,
                "Organization ID cannot be null.");
    }

    /**
     * Activates the person.
     */
    public void activate() {
        this.active = true;
    }

    /**
     * Deactivates the person.
     */
    public void deactivate() {
        this.active = false;
    }

    public PersonId getPersonId() {
        return getId();
    }

    public PersonFullName getFullName() {
        return fullName;
    }

    public OrganizationId getOrganizationId() {
        return organizationId;
    }

    public boolean isActive() {
        return active;
    }
}
