package org.afdb.aikp.modules.iam.infrastructure.persistence.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.UUID;

import org.afdb.aikp.modules.iam.domain.enums.UserStatus;
import org.afdb.aikp.modules.iam.domain.model.User;
import org.afdb.aikp.modules.iam.domain.valueobject.Email;
import org.afdb.aikp.modules.iam.domain.valueobject.FullName;
import org.afdb.aikp.modules.iam.domain.valueobject.PasswordHash;
import org.afdb.aikp.modules.iam.domain.valueobject.UserId;
import org.afdb.aikp.modules.iam.domain.valueobject.Username;
import org.afdb.aikp.modules.iam.infrastructure.persistence.entity.UserEntity;
import org.junit.jupiter.api.Test;

class UserPersistenceMapperTest {

    @Test
    void shouldMapDomainUserToEntity() {

        UUID id = UUID.randomUUID();

        Instant createdAt =
                Instant.parse("2026-01-01T10:00:00Z");

        Instant updatedAt =
                Instant.parse("2026-01-02T11:00:00Z");

        Instant lastLogin =
                Instant.parse("2026-01-03T12:00:00Z");

        User user =
                User.restore(
                        UserId.of(id),
                        Username.of("john.doe"),
                        Email.of("john.doe@example.com"),
                        FullName.of("John Doe"),
                        PasswordHash.of(validPasswordHash()),
                        UserStatus.ACTIVE,
                        createdAt,
                        updatedAt,
                        lastLogin);

        UserEntity entity =
                UserPersistenceMapper.toEntity(user);

        assertThat(entity.getId())
                .isEqualTo(id);

        assertThat(entity.getUsername())
                .isEqualTo("john.doe");

        assertThat(entity.getEmail())
                .isEqualTo("john.doe@example.com");

        assertThat(entity.getFullName())
                .isEqualTo("John Doe");

        assertThat(entity.getPasswordHash())
                .isEqualTo(validPasswordHash());

        assertThat(entity.getStatus())
                .isEqualTo(UserStatus.ACTIVE);

        assertThat(entity.getCreatedAt())
                .isEqualTo(createdAt);

        assertThat(entity.getUpdatedAt())
                .isEqualTo(updatedAt);

        assertThat(entity.getLastLogin())
                .isEqualTo(lastLogin);
    }

    @Test
    void shouldMapEntityToDomainUser() {

        UUID id = UUID.randomUUID();

        Instant createdAt =
                Instant.parse("2026-02-01T10:00:00Z");

        Instant updatedAt =
                Instant.parse("2026-02-02T11:00:00Z");

        Instant lastLogin =
                Instant.parse("2026-02-03T12:00:00Z");

        UserEntity entity =
                new UserEntity(
                        id,
                        "jane.doe",
                        "jane.doe@example.com",
                        "Jane Doe",
                        validPasswordHash(),
                        UserStatus.SUSPENDED,
                        createdAt,
                        updatedAt,
                        lastLogin);

        User user =
                UserPersistenceMapper.toDomain(entity);

        assertThat(user.getId().getValue())
                .isEqualTo(id);

        assertThat(user.getUsername().value())
                .isEqualTo("jane.doe");

        assertThat(user.getEmail().value())
                .isEqualTo("jane.doe@example.com");

        assertThat(user.getFullName().value())
                .isEqualTo("Jane Doe");

        assertThat(user.getPasswordHash().value())
                .isEqualTo(validPasswordHash());

        assertThat(user.getStatus())
                .isEqualTo(UserStatus.SUSPENDED);

        assertThat(user.getCreatedAt())
                .isEqualTo(createdAt);

        assertThat(user.getUpdatedAt())
                .isEqualTo(updatedAt);

        assertThat(user.getLastLogin())
                .isEqualTo(lastLogin);
    }

    @Test
    void shouldMapNullLastLogin() {

        UUID id = UUID.randomUUID();

        Instant createdAt =
                Instant.parse("2026-03-01T10:00:00Z");

        Instant updatedAt =
                Instant.parse("2026-03-02T11:00:00Z");

        UserEntity entity =
                new UserEntity(
                        id,
                        "pending.user",
                        "pending.user@example.com",
                        "Pending User",
                        validPasswordHash(),
                        UserStatus.PENDING,
                        createdAt,
                        updatedAt,
                        null);

        User user =
                UserPersistenceMapper.toDomain(entity);

        assertThat(user.getLastLogin())
                .isNull();
    }

    private static String validPasswordHash() {

        return "$2a$10$abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    }
}
