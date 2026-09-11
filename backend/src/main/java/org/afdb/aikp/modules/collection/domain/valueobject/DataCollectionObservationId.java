package org.afdb.aikp.modules.collection.domain.valueobject;

import java.util.UUID;

import org.afdb.aikp.shared.domain.Identifier;

/**
 * Strongly typed identifier for DataCollectionObservation.
 */
public final class DataCollectionObservationId
        extends Identifier<UUID> {

    private DataCollectionObservationId(UUID value) {
        super(value);
    }

    /**
     * Creates a DataCollectionObservation identifier
     * from an existing UUID.
     */
    public static DataCollectionObservationId of(UUID value) {
        return new DataCollectionObservationId(value);
    }

    /**
     * Generates a new DataCollectionObservation identifier.
     */
    public static DataCollectionObservationId generate() {
        return new DataCollectionObservationId(
                UUID.randomUUID());
    }
}
