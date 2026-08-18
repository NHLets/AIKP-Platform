package org.afdb.aikp.modules.organization.domain.repository;

import org.afdb.aikp.modules.organization.domain.model.Organization;
import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationCode;
import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationId;
import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationType;
import org.afdb.aikp.modules.country.domain.valueobject.CountryId;

import java.util.List;
import java.util.Optional;

/**
 * Repository contract for the Organization aggregate.
 *
 * <p>
 * This interface belongs to the domain layer and is independent
 * of the persistence technology.
 * </p>
 */
public interface OrganizationRepository {

    /**
     * Saves an organization.
     */
    Organization save(Organization organization);

    /**
     * Finds an organization by its identifier.
     */
    Optional<Organization> findById(OrganizationId id);

    /**
     * Finds an organization by its business code.
     */
    Optional<Organization> findByCode(OrganizationCode code);

    /**
     * Returns all organizations.
     */
    List<Organization> findAll();

    /**
     * Returns all active organizations.
     */
    List<Organization> findActive();

    /**
     * Returns organizations belonging to a country.
     */
    List<Organization> findByCountryId(CountryId countryId);

    /**
     * Returns organizations of the given type.
     */
    List<Organization> findByType(OrganizationType type);

    /**
     * Checks whether an organization exists by its identifier.
     */
    boolean existsById(OrganizationId id);

    /**
     * Checks whether an organization exists by its business code.
     */
    boolean existsByCode(OrganizationCode code);

    /**
     * Deletes an organization.
     */
    void delete(Organization organization);
}
