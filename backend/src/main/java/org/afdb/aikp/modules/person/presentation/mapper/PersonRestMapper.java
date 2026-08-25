package org.afdb.aikp.modules.person.presentation.mapper;

import org.afdb.aikp.modules.person.application.command.CreatePersonCommand;
import org.afdb.aikp.modules.person.application.command.UpdatePersonCommand;
import org.afdb.aikp.modules.person.presentation.request.CreatePersonRequest;
import org.afdb.aikp.modules.person.presentation.request.UpdatePersonRequest;

import java.util.UUID;

/**
 * Maps REST requests to application commands.
 */
public final class PersonRestMapper {

    private PersonRestMapper() {
        // Utility class
    }

    /**
     * Maps a create request to an application command.
     */
    public static CreatePersonCommand toCommand(
            CreatePersonRequest request) {

        return new CreatePersonCommand(
                request.fullName(),
                request.organizationId()
        );
    }

    /**
     * Maps an update request to an application command.
     */
    public static UpdatePersonCommand toCommand(
            UUID id,
            UpdatePersonRequest request) {

        return new UpdatePersonCommand(
                id,
                request.fullName(),
                request.organizationId()
        );
    }
}
