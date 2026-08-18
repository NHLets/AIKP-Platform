package org.afdb.aikp.modules.iam.role.application.command;

import org.afdb.aikp.modules.iam.role.domain.valueobject.RoleId;

import java.util.Objects;

/**
 * Command for activating an IAM Role.
 */
public record ActivateRoleCommand(
        RoleId roleId) {

    public ActivateRoleCommand {
        Objects.requireNonNull(roleId);
    }
}
