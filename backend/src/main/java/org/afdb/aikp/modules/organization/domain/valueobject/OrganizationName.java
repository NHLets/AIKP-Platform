package org.afdb.aikp.modules.organization.domain.valueobject;

import java.util.Objects;

/**
 * Value object representing the name
 * of an Organization.
 */
public record OrganizationName(String value) {

    private static final int MAX_LENGTH = 255;

    public OrganizationName {
        Objects.requireNonNull(
                value,
                "Organization name must not be null"
        );

        value = value.trim();

        if (value.isBlank()) {
            throw new IllegalArgumentException(
                    "Organization name must not be blank"
            );
        }

        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "Organization name must not exceed "
                            + MAX_LENGTH + " characters"
            );
        }
    }

    public static OrganizationName of(String value) {
        return new OrganizationName(value);
    }

    public String getValue() {
        return value;
    }
}