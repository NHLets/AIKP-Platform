package org.afdb.aikp.modules.organization.domain.valueobject;

/**
 * Value object representing the optional description
 * of an Organization.
 */
public record OrganizationDescription(String value) {

    private static final int MAX_LENGTH = 2000;

    public OrganizationDescription {
        if (value != null) {
            value = value.trim();

            if (value.length() > MAX_LENGTH) {
                throw new IllegalArgumentException(
                        "Organization description must not exceed "
                                + MAX_LENGTH + " characters"
                );
            }
        }
    }

    public static OrganizationDescription of(String value) {
        return new OrganizationDescription(value);
    }
}
