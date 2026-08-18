package org.afdb.aikp.modules.iam.role.domain.exception;

public class RoleException extends RuntimeException {

    protected RoleException(String message) {
        super(message);
    }

    protected RoleException(String message, Throwable cause) {
        super(message, cause);
    }
}