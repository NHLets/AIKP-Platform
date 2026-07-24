package org.afdb.aikp.shared.exception;

/**
 * Raised when business validation fails.
 */
public abstract class ValidationException extends BusinessException {

    protected ValidationException(String message) {
        super(ErrorCode.VALIDATION_ERROR, message);
    }

    protected ValidationException(
            String message,
            Throwable cause) {

        super(ErrorCode.VALIDATION_ERROR, message, cause);
    }

}