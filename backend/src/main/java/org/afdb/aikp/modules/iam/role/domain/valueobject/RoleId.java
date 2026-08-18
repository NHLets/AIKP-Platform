package org.afdb.aikp.modules.iam.role.domain.valueobject;

import org.afdb.aikp.shared.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

/**
 * Identifier of an IAM Role aggregate.
 */
public final class RoleId extends Identifier<UUID> {

    private RoleId(UUID value) {
        super(Objects.requireNonNull(
                value,
                "RoleId cannot be null"));
    }

    public static RoleId of(UUID value) {
        return new RoleId(value);
    }

    public static RoleId generate() {
        return new RoleId(UUID.randomUUID());
    }
}