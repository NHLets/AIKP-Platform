package org.afdb.aikp.modules.iam.role.infrastructure.persistence.repository;

import org.afdb.aikp.modules.iam.role.domain.enums.RoleStatus;
import org.afdb.aikp.modules.iam.role.infrastructure.persistence.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataRoleRepository
        extends JpaRepository<RoleEntity, UUID> {

    Optional<RoleEntity> findByName(String name);

    List<RoleEntity> findByStatus(RoleStatus status);

    boolean existsByName(String name);
}