package org.afdb.aikp.modules.iam.role.application.response;

import org.afdb.aikp.modules.iam.role.domain.enums.RoleStatus;

import java.util.UUID;

/**
 * Lightweight response representing an IAM Role.
 */
public record RoleSummary(
        UUID id,
        String name,
        RoleStatus status,
        boolean system) {
}
