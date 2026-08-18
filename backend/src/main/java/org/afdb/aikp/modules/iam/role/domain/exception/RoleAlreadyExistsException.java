package org.afdb.aikp.modules.iam.role.domain.exception;

import org.afdb.aikp.shared.exception.ConflictException;

/**
 * Raised when an IAM role already exists.
 */
public final class RoleAlreadyExistsException extends ConflictException {

    private RoleAlreadyExistsException(String message) {
        super(message);
    }

    public static RoleAlreadyExistsException withName(String name) {
        return new RoleAlreadyExistsException(
                "Role with name '%s' already exists.".formatted(name)
        );
    }
}