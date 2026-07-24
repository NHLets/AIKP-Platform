package org.afdb.aikp.modules.country.domain.exception;

import org.afdb.aikp.modules.country.domain.valueobject.CountryId;
import org.afdb.aikp.shared.exception.NotFoundException;

/**
 * Thrown when a Country cannot be found.
 */
public class CountryNotFoundException extends NotFoundException {

    public CountryNotFoundException(CountryId id) {
        super("Country not found with id: " + id.getValue());
    }

}