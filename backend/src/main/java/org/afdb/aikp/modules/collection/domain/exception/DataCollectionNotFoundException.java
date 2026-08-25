package org.afdb.aikp.modules.collection.domain.exception;

import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionId;
import org.afdb.aikp.shared.exception.NotFoundException;

/**
 * Thrown when a DataCollection cannot be found.
 */
public class DataCollectionNotFoundException
        extends NotFoundException {

    public DataCollectionNotFoundException(
            DataCollectionId id) {

        super("Data collection not found with id: "
                + id.getValue());
    }
}
