package org.afdb.aikp.modules.country.domain.valueobject;

import org.afdb.aikp.shared.domain.ValueObject;

import java.util.Locale;

/**
 * ISO 3166-1 Alpha-3 code.
 */
public final class Iso3Code extends ValueObject<String> {

    private Iso3Code(String value) {
        super(normalize(value));
    }

    public static Iso3Code of(String value) {
        return new Iso3Code(value);
    }

    private static String normalize(String value) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("ISO3 code cannot be null or blank.");
        }

        value = value.trim().toUpperCase(Locale.ROOT);

        if (!value.matches("[A-Z]{3}")) {
            throw new IllegalArgumentException("ISO3 code must contain exactly 3 letters.");
        }

        return value;
    }
}