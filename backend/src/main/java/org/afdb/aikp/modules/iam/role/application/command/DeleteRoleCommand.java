package org.afdb.aikp.modules.iam.role.application.command;

import org.afdb.aikp.modules.iam.role.domain.valueobject.RoleId;

import java.util.Objects;

/**
 * Command for deleting an IAM Role.
 */
public record DeleteRoleCommand(
        RoleId roleId) {

    public DeleteRoleCommand {
        Objects.requireNonNull(roleId);
    }
}
