package org.afdb.aikp.modules.organization.domain.exception;

import org.afdb.aikp.shared.exception.ConflictException;

/**
 * Exception thrown when an organization code is already in use.
 */
public class OrganizationCodeAlreadyExistsException
        extends ConflictException {

    public OrganizationCodeAlreadyExistsException(
            String code) {

        super("Organization code already exists: " + code);
    }
}