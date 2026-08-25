package org.afdb.aikp.modules.validation.domain.valueobject;

import java.util.UUID;

import org.afdb.aikp.shared.domain.Identifier;

/**
 * Strongly typed identifier for DataCollectionValidation.
 */
public final class DataCollectionValidationId
        extends Identifier<UUID> {

    private DataCollectionValidationId(UUID value) {
        super(value);
    }

    /**
     * Creates a validation identifier from an existing UUID.
     */
    public static DataCollectionValidationId of(UUID value) {
        return new DataCollectionValidationId(value);
    }

    /**
     * Generates a new validation identifier.
     */
    public static DataCollectionValidationId generate() {
        return new DataCollectionValidationId(
                UUID.randomUUID());
    }
}
