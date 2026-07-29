package org.afdb.aikp.modules.iam.presentation.mapper;

import org.afdb.aikp.modules.iam.application.command.CreateUserCommand;
import org.afdb.aikp.modules.iam.application.command.UpdateUserCommand;
import org.afdb.aikp.modules.iam.presentation.request.CreateUserRequest;
import org.afdb.aikp.modules.iam.presentation.request.UpdateUserRequest;

import java.util.UUID;

public final class UserRestMapper {

    private UserRestMapper() {
    }

    public static CreateUserCommand toCommand(CreateUserRequest request) {

        return new CreateUserCommand(
                request.username(),
                request.email(),
                request.fullName(),
                request.password()
        );
    }

    public static UpdateUserCommand toCommand(
            UUID id,
            UpdateUserRequest request) {

        return new UpdateUserCommand(
                id,
                request.email(),
                request.fullName()
        );
    }
}