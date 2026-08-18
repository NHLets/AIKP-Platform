package org.afdb.aikp.modules.organization.domain.exception;

import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationId;
import org.afdb.aikp.shared.exception.NotFoundException;

/**
 * Exception thrown when an Organization cannot be found.
 */
public class OrganizationNotFoundException
        extends NotFoundException {

    public OrganizationNotFoundException(
            OrganizationId id) {

        super("Organization not found: " + id.getValue());
    }
}
