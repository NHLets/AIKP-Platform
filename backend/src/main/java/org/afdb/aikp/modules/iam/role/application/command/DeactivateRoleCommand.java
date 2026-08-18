package org.afdb.aikp.modules.iam.role.application.command;

import org.afdb.aikp.modules.iam.role.domain.valueobject.RoleId;

import java.util.Objects;

/**
 * Command for deactivating an IAM Role.
 */
public record DeactivateRoleCommand(
        RoleId roleId) {

    public DeactivateRoleCommand {
        Objects.requireNonNull(roleId);
    }
}
