package org.afdb.aikp.modules.iam.domain.valueobject;

import org.afdb.aikp.shared.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public final class UserId extends Identifier<UUID> {

    private UserId(UUID value) {
        super(Objects.requireNonNull(value, "UserId cannot be null"));
    }

    public static UserId of(UUID value) {
        return new UserId(value);
    }

    public static UserId generate() {
        return new UserId(UUID.randomUUID());
    }
}