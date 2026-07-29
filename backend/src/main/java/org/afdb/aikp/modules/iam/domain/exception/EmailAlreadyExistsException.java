package org.afdb.aikp.modules.iam.domain.exception;

public class EmailAlreadyExistsException extends UserException {

    public EmailAlreadyExistsException(String email) {
        super("Email already exists: " + email);
    }
}