package org.afdb.aikp.modules.iam.role.application.query;

import org.afdb.aikp.modules.iam.role.domain.valueobject.RoleId;

import java.util.Objects;

/**
 * Query for retrieving a single IAM Role.
 */
public record GetRoleQuery(
        RoleId roleId) {

    public GetRoleQuery {
        Objects.requireNonNull(roleId);
    }
}
