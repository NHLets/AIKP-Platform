package org.afdb.aikp.modules.country.domain.exception;

import org.afdb.aikp.shared.exception.ConflictException;

/**
 * Thrown when a Country violates uniqueness constraints.
 */
public class DuplicateCountryException extends ConflictException {

    public DuplicateCountryException(String message) {
        super(message);
    }

}