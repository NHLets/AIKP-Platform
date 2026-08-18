package org.afdb.aikp.modules.iam.role.application.mapper;

import org.afdb.aikp.modules.iam.role.application.response.RoleResponse;
import org.afdb.aikp.modules.iam.role.application.response.RoleSummary;
import org.afdb.aikp.modules.iam.role.domain.model.Role;

/**
 * Mapper between the Role domain model and application responses.
 */
public final class RoleApplicationMapper {

    private RoleApplicationMapper() {
        // Utility class.
    }

    /**
     * Maps a Role aggregate to a detailed response.
     */
    public static RoleResponse toResponse(Role role) {

        if (role == null) {
            return null;
        }

        return new RoleResponse(
                role.getId().getValue(),
                role.getName().value(),
                role.getDescription().value(),
                role.getStatus(),
                role.isSystem(),
                role.getCreatedAt(),
                role.getUpdatedAt()
        );
    }

    /**
     * Maps a Role aggregate to a lightweight summary.
     */
    public static RoleSummary toSummary(Role role) {

        if (role == null) {
            return null;
        }

        return new RoleSummary(
                role.getId().getValue(),
                role.getName().value(),
                role.getStatus(),
                role.isSystem()
        );
    }
}
