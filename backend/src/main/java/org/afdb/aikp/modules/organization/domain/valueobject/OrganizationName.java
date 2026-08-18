package org.afdb.aikp.modules.organization.domain.valueobject;

import org.afdb.aikp.shared.domain.ValueObject;

/**
 * Name of an Organization.
 */
public final class OrganizationName extends ValueObject<String> {

    private OrganizationName(String value) {
        super(normalize(value));
    }

    /**
     * Creates an OrganizationName.
     */
    public static OrganizationName of(String value) {
        return new OrganizationName(value);
    }

    private static String normalize(String value) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Organization name cannot be null or blank.");
        }

        value = value.trim();

        if (value.length() > 255) {
            throw new IllegalArgumentException(
                    "Organization name cannot exceed 255 characters.");
        }

        return value;
    }
}
