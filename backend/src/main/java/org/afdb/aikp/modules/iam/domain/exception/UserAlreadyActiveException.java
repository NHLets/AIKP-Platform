package org.afdb.aikp.modules.iam.domain.exception;

import java.util.UUID;

public final class UserAlreadyActiveException extends UserException {

    private UserAlreadyActiveException(String message) {
        super(message);
    }

    public static UserAlreadyActiveException withId(UUID id) {
        return new UserAlreadyActiveException(
                "User with id '%s' is already active.".formatted(id)
        );
    }
}