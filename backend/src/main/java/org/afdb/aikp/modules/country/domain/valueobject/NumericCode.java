package org.afdb.aikp.modules.country.domain.valueobject;

import org.afdb.aikp.shared.domain.ValueObject;

/**
 * ISO 3166-1 Numeric code.
 */
public final class NumericCode extends ValueObject<String> {

    private NumericCode(String value) {
        super(normalize(value));
    }

    public static NumericCode of(String value) {
        return new NumericCode(value);
    }

    private static String normalize(String value) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Numeric code cannot be null or blank.");
        }

        value = value.trim();

        if (!value.matches("\\d{3}")) {
            throw new IllegalArgumentException("Numeric code must contain exactly 3 digits.");
        }

        return value;
    }
}