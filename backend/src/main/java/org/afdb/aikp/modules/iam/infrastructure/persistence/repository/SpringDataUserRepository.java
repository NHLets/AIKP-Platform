package org.afdb.aikp.modules.iam.infrastructure.persistence.repository;

import org.afdb.aikp.modules.iam.domain.enums.UserStatus;
import org.afdb.aikp.modules.iam.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataUserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByUsername(String username);

    @EntityGraph(attributePaths = {"role"})
    Optional<UserEntity> findByEmail(String email);

    List<UserEntity> findByStatus(UserStatus status);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}