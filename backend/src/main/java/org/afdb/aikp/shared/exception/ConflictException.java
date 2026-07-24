package org.afdb.aikp.shared.exception;

/**
 * Raised when a business conflict occurs.
 */
public abstract class ConflictException extends BusinessException {

    protected ConflictException(String message) {
        super(ErrorCode.CONFLICT, message);
    }

    protected ConflictException(
            String message,
            Throwable cause) {

        super(ErrorCode.CONFLICT, message, cause);
    }

}