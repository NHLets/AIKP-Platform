package org.afdb.aikp.modules.iam.infrastructure.persistence.mapper;

import org.afdb.aikp.modules.iam.role.infrastructure.persistence.entity.RoleEntity;
import org.afdb.aikp.modules.iam.role.domain.model.Role;
import org.afdb.aikp.modules.iam.domain.model.User;
import org.afdb.aikp.modules.iam.domain.valueobject.Email;
import org.afdb.aikp.modules.iam.domain.valueobject.FullName;
import org.afdb.aikp.modules.iam.domain.valueobject.PasswordHash;
import org.afdb.aikp.modules.iam.domain.valueobject.UserId;
import org.afdb.aikp.modules.iam.domain.valueobject.Username;
import org.afdb.aikp.modules.iam.infrastructure.persistence.entity.UserEntity;

public final class UserPersistenceMapper {

    private UserPersistenceMapper() {
    }

    public static UserEntity toEntity(User user) {

        return new UserEntity(
                user.getId().getValue(),
                user.getUsername().value(),
                user.getEmail().value(),
                user.getFullName().value(),
                user.getPasswordHash().value(),
                user.getStatus(),
                toEntityRole(user.getRole()),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getLastLogin()
        );
    }

    public static User toDomain(UserEntity entity) {

        return User.restore(
                UserId.of(entity.getId()),
                Username.of(entity.getUsername()),
                Email.of(entity.getEmail()),
                FullName.of(entity.getFullName()),
                PasswordHash.of(entity.getPasswordHash()),
                entity.getStatus(),
                toDomainRole(entity.getRole()),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getLastLogin()
        );
    }

    private Role toDomainRole(RoleEntity entity) {
        if (entity == null) {
            return null;
        }

        return Role.restore(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getStatus(),
                entity.isSystem(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    private RoleEntity toEntityRole(Role role) {
        if (role == null) {
            return null;
        }

        return new RoleEntity(
                role.getId().value(),
                role.getName().value(),
                role.getDescription().value(),
                role.getStatus(),
                role.isSystem(),
                role.getCreatedAt(),
                role.getUpdatedAt()
        );
    }

}