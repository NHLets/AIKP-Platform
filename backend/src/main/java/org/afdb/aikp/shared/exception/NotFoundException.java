package org.afdb.aikp.shared.exception;

/**
 * Raised when a requested resource cannot be found.
 */
public abstract class NotFoundException extends BusinessException {

    protected NotFoundException(String message) {
        super(ErrorCode.NOT_FOUND, message);
    }

    protected NotFoundException(
            String message,
            Throwable cause) {

        super(ErrorCode.NOT_FOUND, message, cause);
    }

}