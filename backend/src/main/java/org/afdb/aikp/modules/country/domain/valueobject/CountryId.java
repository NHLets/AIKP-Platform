package org.afdb.aikp.modules.country.domain.valueobject;

import org.afdb.aikp.shared.domain.Identifier;

import java.util.UUID;

/**
 * Strongly typed identifier for Country.
 */
public final class CountryId extends Identifier<UUID> {

    private CountryId(UUID value) {
        super(value);
    }

    public static CountryId of(UUID value) {
        return new CountryId(value);
    }

    public static CountryId generate() {
        return new CountryId(UUID.randomUUID());
    }
}