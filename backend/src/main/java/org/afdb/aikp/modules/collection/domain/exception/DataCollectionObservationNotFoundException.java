package org.afdb.aikp.modules.collection.domain.exception;

import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionObservationId;
import org.afdb.aikp.shared.exception.NotFoundException;

/**
 * Thrown when a DataCollectionObservation cannot be found.
 */
public class DataCollectionObservationNotFoundException
        extends NotFoundException {

    public DataCollectionObservationNotFoundException(
            DataCollectionObservationId id) {

        super("Data collection observation not found with id: "
                + id.getValue());
    }
}
