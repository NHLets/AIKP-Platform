package org.afdb.aikp.modules.iam.domain.exception;

public class UserNotFoundException extends UserException {

    public UserNotFoundException(String message) {
        super(message);
    }

    public static UserNotFoundException withId(Object id) {
        return new UserNotFoundException("User not found: " + id);
    }
}