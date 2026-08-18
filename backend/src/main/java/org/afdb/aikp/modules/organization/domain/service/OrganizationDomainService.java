package org.afdb.aikp.modules.organization.domain.service;

import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationCode;
import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationId;

/**
 * Domain service for the Organization aggregate.
 */
public interface OrganizationDomainService {

    /**
     * Validates that an organization can be created.
     */
    void validateCreation(
            OrganizationCode code);

    /**
     * Validates that an organization can be updated.
     */
    void validateUpdate(
            OrganizationId organizationId,
            OrganizationCode code);
}
