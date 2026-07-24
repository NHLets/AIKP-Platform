package org.afdb.aikp.modules.country.domain.valueobject;

import org.afdb.aikp.shared.domain.ValueObject;

/**
 * Short country name.
 */
public final class CountryName extends ValueObject<String> {

    private CountryName(String value) {
        super(normalize(value));
    }

    public static CountryName of(String value) {
        return new CountryName(value);
    }

    private static String normalize(String value) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Country name cannot be null or blank.");
        }

        value = value.trim();

        if (value.length() > 100) {
            throw new IllegalArgumentException("Country name cannot exceed 100 characters.");
        }

        return value;
    }
}