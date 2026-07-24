package org.afdb.aikp.modules.country.domain.valueobject;

import org.afdb.aikp.shared.domain.ValueObject;

import java.util.Locale;

/**
 * ISO 3166-1 Alpha-2 code.
 */
public final class Iso2Code extends ValueObject<String> {

    private Iso2Code(String value) {
        super(normalize(value));
    }

    public static Iso2Code of(String value) {
        return new Iso2Code(value);
    }

    private static String normalize(String value) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("ISO2 code cannot be null or blank.");
        }

        value = value.trim().toUpperCase(Locale.ROOT);

        if (!value.matches("[A-Z]{2}")) {
            throw new IllegalArgumentException("ISO2 code must contain exactly 2 letters.");
        }

        return value;
    }
}