package org.afdb.aikp.modules.iam.role.application.command;

import org.afdb.aikp.modules.iam.role.domain.valueobject.RoleDescription;
import org.afdb.aikp.modules.iam.role.domain.valueobject.RoleName;

import java.util.Objects;

/**
 * Command for creating an IAM Role.
 */
public record CreateRoleCommand(
        RoleName name,
        RoleDescription description,
        boolean system) {

    public CreateRoleCommand {
        Objects.requireNonNull(name);
        Objects.requireNonNull(description);
    }
}
