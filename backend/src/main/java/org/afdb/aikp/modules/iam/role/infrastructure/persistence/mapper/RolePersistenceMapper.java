package org.afdb.aikp.modules.iam.role.infrastructure.persistence.mapper;

import org.afdb.aikp.modules.iam.role.domain.model.Role;
import org.afdb.aikp.modules.iam.role.domain.valueobject.RoleDescription;
import org.afdb.aikp.modules.iam.role.domain.valueobject.RoleId;
import org.afdb.aikp.modules.iam.role.domain.valueobject.RoleName;
import org.afdb.aikp.modules.iam.role.infrastructure.persistence.entity.RoleEntity;

public final class RolePersistenceMapper {

    private RolePersistenceMapper() {
    }

    public static RoleEntity toEntity(Role role) {

        return new RoleEntity(
                role.getId().getValue(),
                role.getName().value(),
                role.getDescription().value(),
                role.getStatus(),
                role.isSystem(),
                role.getCreatedAt(),
                role.getUpdatedAt()
        );
    }

    public static Role toDomain(RoleEntity entity) {

        return Role.restore(
                RoleId.of(entity.getId()),
                RoleName.of(entity.getName()),
                RoleDescription.of(entity.getDescription()),
                entity.getStatus(),
                entity.isSystem(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}