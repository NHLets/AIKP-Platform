package org.afdb.aikp.modules.iam.infrastructure.persistence.mapper;

import org.afdb.aikp.modules.iam.domain.model.User;
import org.afdb.aikp.modules.iam.domain.valueobject.Email;
import org.afdb.aikp.modules.iam.domain.valueobject.FullName;
import org.afdb.aikp.modules.iam.domain.valueobject.PasswordHash;
import org.afdb.aikp.modules.iam.domain.valueobject.UserId;
import org.afdb.aikp.modules.iam.domain.valueobject.Username;
import org.afdb.aikp.modules.iam.infrastructure.persistence.entity.UserEntity;
import org.afdb.aikp.modules.iam.role.infrastructure.persistence.mapper.RolePersistenceMapper;
import org.afdb.aikp.modules.iam.role.domain.model.Role;

public final class UserPersistenceMapper {

    private UserPersistenceMapper() {}

    public static UserEntity toEntity(User user) {

        UserEntity entity = new UserEntity(
                user.getId().getValue(),
                user.getUsername().value(),
                user.getEmail().value(),
                user.getFullName().value(),
                user.getPasswordHash().value(),
                user.getStatus(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getLastLogin()
        );

        if (user.getRole() != null) {
            entity.setRole(
                    RolePersistenceMapper.toEntity(user.getRole())
            );
        }

        return entity;
    }

    public static User toDomain(UserEntity entity) {

        User user = User.restore(
                UserId.of(entity.getId()),
                Username.of(entity.getUsername()),
                Email.of(entity.getEmail()),
                FullName.of(entity.getFullName()),
                PasswordHash.of(entity.getPasswordHash()),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getLastLogin()
        );

        if (entity.getRole() != null) {
            Role role = RolePersistenceMapper.toDomain(
                    entity.getRole()
            );
            user.assignRole(role);
        }

        return user;
    }
}
