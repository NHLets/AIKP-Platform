package org.afdb.aikp.modules.country.domain.exception;

import org.afdb.aikp.shared.exception.ValidationException;

/**
 * Thrown when a Country violates business validation rules.
 */
public class InvalidCountryException extends ValidationException {

    public InvalidCountryException(String message) {
        super(message);
    }

}