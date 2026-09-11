package org.afdb.aikp.modules.collection.domain.enums;

/**
 * Status of a collected observation.
 */
public enum ObservationStatus {

    /**
     * A value has been provided for the observation.
     */
    PROVIDED,

    /**
     * The requested information is not available.
     */
    NOT_AVAILABLE,

    /**
     * The variable is not applicable to the collection context.
     */
    NOT_APPLICABLE
}
