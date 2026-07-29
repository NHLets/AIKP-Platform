package org.afdb.aikp.modules.iam.domain.exception;

public abstract class UserException extends RuntimeException {

    protected UserException(String message) {
        super(message);
    }
}