package org.afdb.aikp.modules.iam.role.presentation.mapper;

import org.afdb.aikp.modules.iam.role.application.command.CreateRoleCommand;
import org.afdb.aikp.modules.iam.role.application.command.UpdateRoleCommand;
import org.afdb.aikp.modules.iam.role.domain.valueobject.RoleDescription;
import org.afdb.aikp.modules.iam.role.domain.valueobject.RoleId;
import org.afdb.aikp.modules.iam.role.domain.valueobject.RoleName;
import org.afdb.aikp.modules.iam.role.presentation.request.CreateRoleRequest;
import org.afdb.aikp.modules.iam.role.presentation.request.UpdateRoleRequest;

import java.util.UUID;

/**
 * Maps REST requests to application commands.
 */
public final class RoleRestMapper {

    private RoleRestMapper() {
        // Utility class.
    }

    /**
     * Maps a create request to an application command.
     */
    public static CreateRoleCommand toCommand(
            CreateRoleRequest request) {

        return new CreateRoleCommand(
                RoleName.of(request.name()),
                RoleDescription.of(request.description()),
                request.system()
        );
    }

    /**
     * Maps an update request to an application command.
     */
    public static UpdateRoleCommand toCommand(
            UUID id,
            UpdateRoleRequest request) {

        return new UpdateRoleCommand(
                RoleId.of(id),
                RoleName.of(request.name()),
                RoleDescription.of(request.description())
        );
    }
}
