package org.afdb.aikp.modules.organization.domain.valueobject;

import org.afdb.aikp.shared.domain.Identifier;

import java.util.UUID;

/**
 * Strongly typed identifier for Organization.
 */
public final class OrganizationId extends Identifier<UUID> {

    private OrganizationId(UUID value) {
        super(value);
    }

    /**
     * Creates an Organization identifier from an existing UUID.
     */
    public static OrganizationId of(UUID value) {
        return new OrganizationId(value);
    }

    /**
     * Generates a new Organization identifier.
     */
    public static OrganizationId generate() {
        return new OrganizationId(UUID.randomUUID());
    }
}