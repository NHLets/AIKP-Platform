package org.afdb.aikp.modules.organization.domain.model;

import org.afdb.aikp.modules.country.domain.valueobject.CountryId;
import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationCode;
import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationId;
import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationName;
import org.afdb.aikp.modules.organization.domain.enums.OrganizationType;
import org.afdb.aikp.shared.domain.AggregateRoot;

import java.util.Objects;

/**
 * Organization Aggregate Root.
 *
 * <p>
 * Represents an institution or organization participating in the
 * AIKP ecosystem.
 * </p>
 */
public final class Organization
        extends AggregateRoot<OrganizationId> {

    private OrganizationCode code;

    private OrganizationName name;

    private OrganizationType type;

    private CountryId countryId;

    private boolean active;

    /**
     * Private constructor.
     * Instances must be created through the factory method.
     */
    private Organization(
            OrganizationId id,
            OrganizationCode code,
            OrganizationName name,
            OrganizationType type,
            CountryId countryId,
            boolean active) {

        super(Objects.requireNonNull(id));

        this.code = Objects.requireNonNull(code);
        this.name = Objects.requireNonNull(name);
        this.type = Objects.requireNonNull(type);
        this.countryId = Objects.requireNonNull(countryId);
        this.active = active;
    }

    /**
     * Creates a new Organization.
     */
    public static Organization create(
            OrganizationId id,
            OrganizationCode code,
            OrganizationName name,
            OrganizationType type,
            CountryId countryId) {

        return new Organization(
                id,
                code,
                name,
                type,
                countryId,
                true
        );
    }

    /**
     * Rebuilds an existing Organization aggregate from persistence.
     */
    public static Organization restore(
            OrganizationId id,
            OrganizationCode code,
            OrganizationName name,
            OrganizationType type,
            CountryId countryId,
            boolean active) {

        return new Organization(
                id,
                code,
                name,
                type,
                countryId,
                active
        );
    }

    /**
     * Changes the organization's code.
     */
    public void changeCode(OrganizationCode code) {
        this.code = Objects.requireNonNull(code);
    }

    /**
     * Changes the organization's name.
     */
    public void rename(OrganizationName name) {
        this.name = Objects.requireNonNull(name);
    }

    /**
     * Changes the organization's type.
     */
    public void changeType(OrganizationType type) {
        this.type = Objects.requireNonNull(type);
    }

    /**
     * Changes the country associated with the organization.
     */
    public void changeCountry(CountryId countryId) {
        this.countryId = Objects.requireNonNull(countryId);
    }

    /**
     * Activates the organization.
     */
    public void activate() {
        this.active = true;
    }

    /**
     * Deactivates the organization.
     */
    public void deactivate() {
        this.active = false;
    }

    public OrganizationId getOrganizationId() {
        return getId();
    }

    public OrganizationCode getCode() {
        return code;
    }

    public OrganizationName getName() {
        return name;
    }

    public OrganizationType getType() {
        return type;
    }

    public CountryId getCountryId() {
        return countryId;
    }

    public boolean isActive() {
        return active;
    }
}
