package org.afdb.aikp.modules.validation.domain.exception;

import java.util.UUID;

import org.afdb.aikp.modules.collection.domain.enums.DataCollectionStatus;

/**
 * Raised when a data collection cannot be validated because
 * its current lifecycle status does not allow validation.
 */
public class DataCollectionValidationNotAllowedException
        extends RuntimeException {

    public DataCollectionValidationNotAllowedException(
            UUID dataCollectionId,
            DataCollectionStatus status) {

        super(
                "Data collection " + dataCollectionId
                        + " cannot be validated in status "
                        + status + ".");
    }
}
