package org.afdb.aikp.modules.organization.domain.valueobject;

import org.afdb.aikp.shared.domain.ValueObject;

/**
 * Business code identifying an Organization.
 */
public final class OrganizationCode extends ValueObject<String> {

    private OrganizationCode(String value) {
        super(normalize(value));
    }

    /**
     * Creates an OrganizationCode.
     */
    public static OrganizationCode of(String value) {
        return new OrganizationCode(value);
    }

    private static String normalize(String value) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Organization code cannot be null or blank.");
        }

        value = value.trim().toUpperCase();

        if (value.length() > 50) {
            throw new IllegalArgumentException(
                    "Organization code cannot exceed 50 characters.");
        }

        return value;
    }
}
