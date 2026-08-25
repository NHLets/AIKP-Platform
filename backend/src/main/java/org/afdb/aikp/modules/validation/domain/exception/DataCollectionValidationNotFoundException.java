package org.afdb.aikp.modules.validation.domain.exception;

import org.afdb.aikp.modules.validation.domain.valueobject.DataCollectionValidationId;

/**
 * Thrown when a data collection validation cannot be found.
 */
public class DataCollectionValidationNotFoundException
        extends RuntimeException {

    public DataCollectionValidationNotFoundException(
            DataCollectionValidationId id) {

        super(
                "Data collection validation not found: "
                        + id.getValue());
    }
}
