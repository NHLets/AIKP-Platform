package org.afdb.aikp.modules.iam.domain.exception;

import java.util.UUID;

import org.afdb.aikp.shared.exception.ConflictException;

/**
 * Thrown when a user cannot be deleted because validation history references it.
 */
public class UserInUseException extends ConflictException {

    public UserInUseException(UUID userId) {
        super(
                "User " + userId
                        + " cannot be deleted because the user is referenced "
                        + "in the data collection validation history. "
                        + "The validation history must be preserved.");
    }
}
