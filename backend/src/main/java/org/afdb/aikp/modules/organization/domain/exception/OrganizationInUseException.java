package org.afdb.aikp.modules.organization.domain.exception;

import java.util.UUID;

import org.afdb.aikp.shared.exception.ConflictException;

/**
 * Thrown when an organization is referenced by one or more Data Collections.
 */
public class OrganizationInUseException extends ConflictException {

    public OrganizationInUseException(UUID organizationId) {
        super(
                "Organization " + organizationId
                        + " cannot be deleted because it is involved "
                        + "in a data collection.");
    }
}
