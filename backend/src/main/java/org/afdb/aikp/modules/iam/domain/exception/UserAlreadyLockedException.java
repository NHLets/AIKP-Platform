package org.afdb.aikp.modules.iam.domain.exception;

import java.util.UUID;

public class UserAlreadyLockedException extends UserException {

    private UserAlreadyLockedException(String message) {
        super(message);
    }

    public static UserAlreadyLockedException withId(UUID id) {
        return new UserAlreadyLockedException(
                "User '%s' is already locked.".formatted(id)
        );
    }
}