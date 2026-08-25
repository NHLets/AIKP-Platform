package org.afdb.aikp.modules.collection.domain.enums;

/**
 * Lifecycle status of a DataCollection.
 */
public enum DataCollectionStatus {

    /**
     * Collection has been created but data collection
     * has not yet started.
     */
    DRAFT,

    /**
     * Data collection is currently in progress.
     */
    IN_PROGRESS,

    /**
     * Data collection has been submitted for validation.
     */
    SUBMITTED,

    /**
     * Data collection has been validated.
     */
    VALIDATED,

    /**
     * Data collection has been rejected during validation.
     */
    REJECTED,

    /**
     * Data collection has been cancelled.
     */
    CANCELLED
}
