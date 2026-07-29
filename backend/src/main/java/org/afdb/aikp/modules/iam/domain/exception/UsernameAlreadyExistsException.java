package org.afdb.aikp.modules.iam.domain.exception;

public class UsernameAlreadyExistsException extends UserException {

    public UsernameAlreadyExistsException(String username) {
        super("Username already exists: " + username);
    }
}