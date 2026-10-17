package org.afdb.aikp.modules.collection.domain.exception;

import org.afdb.aikp.modules.collection.domain.enums.DataCollectionStatus;
import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionId;
import org.afdb.aikp.shared.exception.ConflictException;

/**
 * Thrown when a DataCollection cannot be deleted because
 * its lifecycle status does not allow deletion.
 */
public class DataCollectionDeletionNotAllowedException
        extends ConflictException {

    public DataCollectionDeletionNotAllowedException(
            DataCollectionId id,
            DataCollectionStatus status) {

        super(
                "Data collection " + id.getValue()
                        + " cannot be deleted because its status is "
                        + status
                        + ". Only DRAFT or IN_PROGRESS data collections "
                        + "can be deleted.");
    }
}
