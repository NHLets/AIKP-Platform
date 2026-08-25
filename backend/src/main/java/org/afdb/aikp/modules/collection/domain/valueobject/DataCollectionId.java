package org.afdb.aikp.modules.collection.domain.valueobject;

import java.util.UUID;

import org.afdb.aikp.shared.domain.Identifier;

/**
 * Strongly typed identifier for DataCollection.
 */
public final class DataCollectionId extends Identifier<UUID> {

    private DataCollectionId(UUID value) {
        super(value);
    }

    /**
     * Creates a DataCollection identifier from an existing UUID.
     */
    public static DataCollectionId of(UUID value) {
        return new DataCollectionId(value);
    }

    /**
     * Generates a new DataCollection identifier.
     */
    public static DataCollectionId generate() {
        return new DataCollectionId(UUID.randomUUID());
    }
}
