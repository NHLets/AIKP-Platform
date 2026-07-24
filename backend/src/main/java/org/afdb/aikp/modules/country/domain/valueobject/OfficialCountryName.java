package org.afdb.aikp.modules.country.domain.valueobject;

import org.afdb.aikp.shared.domain.ValueObject;

/**
 * Official country name.
 */
public final class OfficialCountryName extends ValueObject<String> {

    private OfficialCountryName(String value) {
        super(normalize(value));
    }

    public static OfficialCountryName of(String value) {
        return new OfficialCountryName(value);
    }

    private static String normalize(String value) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Official country name cannot be null or blank.");
        }

        value = value.trim();

        if (value.length() > 200) {
            throw new IllegalArgumentException("Official country name cannot exceed 200 characters.");
        }

        return value;
    }
}