package org.afdb.aikp.modules.iam.role.application.response;

import org.afdb.aikp.modules.iam.role.domain.enums.RoleStatus;

import java.time.Instant;
import java.util.UUID;

/**
 * Detailed response representing an IAM Role.
 */
public record RoleResponse(
        UUID id,
        String name,
        String description,
        RoleStatus status,
        boolean system,
        Instant createdAt,
        Instant updatedAt) {
}
