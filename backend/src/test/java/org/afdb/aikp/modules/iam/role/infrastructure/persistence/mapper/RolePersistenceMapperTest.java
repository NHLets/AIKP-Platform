package org.afdb.aikp.modules.iam.role.infrastructure.persistence.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.UUID;

import org.afdb.aikp.modules.iam.role.domain.enums.RoleStatus;
import org.afdb.aikp.modules.iam.role.domain.model.Role;
import org.afdb.aikp.modules.iam.role.domain.valueobject.RoleDescription;
import org.afdb.aikp.modules.iam.role.domain.valueobject.RoleId;
import org.afdb.aikp.modules.iam.role.domain.valueobject.RoleName;
import org.afdb.aikp.modules.iam.role.infrastructure.persistence.entity.RoleEntity;

import org.junit.jupiter.api.Test;

class RolePersistenceMapperTest {

    @Test
    void shouldMapDomainToEntity() {

        Instant createdAt =
                Instant.parse(
                        "2026-01-01T10:00:00Z");

        Instant updatedAt =
                Instant.parse(
                        "2026-01-02T10:00:00Z");

        Role role =
                Role.restore(
                        RoleId.of(
                                UUID.randomUUID()),
                        RoleName.of(
                                "DATA_MANAGER"),
                        RoleDescription.of(
                                "Manages data collection"),
                        RoleStatus.ACTIVE,
                        false,
                        createdAt,
                        updatedAt);

        RoleEntity entity =
                RolePersistenceMapper.toEntity(
                        role);

        assertThat(entity)
                .isNotNull();

        assertThat(entity.getId())
                .isEqualTo(
                        role.getId().getValue());

        assertThat(entity.getName())
                .isEqualTo(
                        role.getName().value());

        assertThat(entity.getDescription())
                .isEqualTo(
                        role.getDescription().value());

        assertThat(entity.getStatus())
                .isEqualTo(
                        role.getStatus());

        assertThat(entity.isSystem())
                .isEqualTo(
                        role.isSystem());

        assertThat(entity.getCreatedAt())
                .isEqualTo(
                        role.getCreatedAt());

        assertThat(entity.getUpdatedAt())
                .isEqualTo(
                        role.getUpdatedAt());
    }


    @Test
    void shouldMapEntityToDomain() {

        UUID id =
                UUID.randomUUID();

        Instant createdAt =
                Instant.parse(
                        "2026-02-01T10:00:00Z");

        Instant updatedAt =
                Instant.parse(
                        "2026-02-02T10:00:00Z");

        RoleEntity entity =
                new RoleEntity(
                        id,
                        "ADMINISTRATOR",
                        "System administrator",
                        RoleStatus.INACTIVE,
                        true,
                        createdAt,
                        updatedAt);

        Role role =
                RolePersistenceMapper.toDomain(
                        entity);

        assertThat(role)
                .isNotNull();

        assertThat(role.getId().getValue())
                .isEqualTo(id);

        assertThat(role.getName().value())
                .isEqualTo(
                        "ADMINISTRATOR");

        assertThat(role.getDescription().value())
                .isEqualTo(
                        "System administrator");

        assertThat(role.getStatus())
                .isEqualTo(
                        RoleStatus.INACTIVE);

        assertThat(role.isSystem())
                .isTrue();

        assertThat(role.getCreatedAt())
                .isEqualTo(
                        createdAt);

        assertThat(role.getUpdatedAt())
                .isEqualTo(
                        updatedAt);
    }


    @Test
    void shouldPreserveAllFieldsDuringRoundTrip() {

        Role original =
                Role.restore(
                        RoleId.of(
                                UUID.randomUUID()),
                        RoleName.of(
                                "REVIEWER"),
                        RoleDescription.of(
                                "Reviews submitted data"),
                        RoleStatus.ACTIVE,
                        true,
                        Instant.parse(
                                "2026-03-01T10:00:00Z"),
                        Instant.parse(
                                "2026-03-02T10:00:00Z"));

        RoleEntity entity =
                RolePersistenceMapper.toEntity(
                        original);

        Role restored =
                RolePersistenceMapper.toDomain(
                        entity);

        assertThat(restored.getId())
                .isEqualTo(
                        original.getId());

        assertThat(restored.getName())
                .isEqualTo(
                        original.getName());

        assertThat(restored.getDescription())
                .isEqualTo(
                        original.getDescription());

        assertThat(restored.getStatus())
                .isEqualTo(
                        original.getStatus());

        assertThat(restored.isSystem())
                .isEqualTo(
                        original.isSystem());

        assertThat(restored.getCreatedAt())
                .isEqualTo(
                        original.getCreatedAt());

        assertThat(restored.getUpdatedAt())
                .isEqualTo(
                        original.getUpdatedAt());
    }
}
