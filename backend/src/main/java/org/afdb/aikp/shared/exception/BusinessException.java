package org.afdb.aikp.shared.exception;

/**
 * Base class for all business exceptions.
 */
public abstract class BusinessException extends AikpException {

    protected BusinessException(
            ErrorCode errorCode,
            String message) {

        super(errorCode, message);
    }

    protected BusinessException(
            ErrorCode errorCode,
            String message,
            Throwable cause) {

        super(errorCode, message, cause);
    }

}