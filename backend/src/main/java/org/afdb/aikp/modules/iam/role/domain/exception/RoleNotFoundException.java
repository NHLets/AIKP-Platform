package org.afdb.aikp.modules.iam.role.domain.exception;

import org.afdb.aikp.shared.exception.NotFoundException;

import java.util.UUID;

/**
 * Raised when an IAM role cannot be found.
 */
public final class RoleNotFoundException extends NotFoundException {

    private RoleNotFoundException(String message) {
        super(message);
    }

    public static RoleNotFoundException withId(UUID id) {
        return new RoleNotFoundException(
                "Role with id '%s' was not found.".formatted(id)
        );
    }
}