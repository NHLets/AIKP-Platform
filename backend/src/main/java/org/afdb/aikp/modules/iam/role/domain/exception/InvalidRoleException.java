package org.afdb.aikp.modules.iam.role.domain.exception;

public final class InvalidRoleException extends RoleException {

    private InvalidRoleException(String message) {
        super(message);
    }

    public static InvalidRoleException withMessage(String message) {
        return new InvalidRoleException(message);
    }
}