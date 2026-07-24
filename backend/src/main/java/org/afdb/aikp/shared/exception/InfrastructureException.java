package org.afdb.aikp.shared.exception;

/**
 * Raised when an infrastructure error occurs.
 */
public class InfrastructureException extends AikpException {

    public InfrastructureException(String message) {
        super(ErrorCode.INFRASTRUCTURE_ERROR, message);
    }

    public InfrastructureException(
            String message,
            Throwable cause) {

        super(ErrorCode.INFRASTRUCTURE_ERROR, message, cause);
    }

}