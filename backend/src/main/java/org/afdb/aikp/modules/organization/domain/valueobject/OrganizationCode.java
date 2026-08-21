package org.afdb.aikp.modules.organization.domain.valueobject;

import java.util.Objects;

/**
 * Value object representing the business code
 * of an Organization.
 */
public record OrganizationCode(String value) {

    private static final int MAX_LENGTH = 50;

    public OrganizationCode {
        Objects.requireNonNull(
                value,
                "Organization code must not be null"
        );

        value = value.trim();

        if (value.isBlank()) {
            throw new IllegalArgumentException(
                    "Organization code must not be blank"
            );
        }

        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "Organization code must not exceed "
                            + MAX_LENGTH + " characters"
            );
        }
    }

    public static OrganizationCode of(String value) {
        return new OrganizationCode(value);
    }

    public String getValue() {
        return value;
    }
}