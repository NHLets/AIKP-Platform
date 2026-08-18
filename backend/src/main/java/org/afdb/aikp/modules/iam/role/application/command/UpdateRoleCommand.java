package org.afdb.aikp.modules.iam.role.application.command;

import org.afdb.aikp.modules.iam.role.domain.valueobject.RoleDescription;
import org.afdb.aikp.modules.iam.role.domain.valueobject.RoleId;
import org.afdb.aikp.modules.iam.role.domain.valueobject.RoleName;

import java.util.Objects;

/**
 * Command for updating an IAM Role.
 */
public record UpdateRoleCommand(
        RoleId roleId,
        RoleName name,
        RoleDescription description) {

    public UpdateRoleCommand {
        Objects.requireNonNull(roleId);
        Objects.requireNonNull(name);
        Objects.requireNonNull(description);
    }
}
