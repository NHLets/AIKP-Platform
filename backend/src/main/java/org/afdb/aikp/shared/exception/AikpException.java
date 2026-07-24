package org.afdb.aikp.shared.exception;

/**
 * Root exception for the AIKP Platform.
 */
public abstract class AikpException extends RuntimeException {

    private final ErrorCode errorCode;

    protected AikpException(
            ErrorCode errorCode,
            String message) {

        super(message);

        this.errorCode = errorCode;
    }

    protected AikpException(
            ErrorCode errorCode,
            String message,
            Throwable cause) {

        super(message, cause);

        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

}