package org.afdb.aikp.modules.passwordreset.infrastructure.persistence.repository;

import org.afdb.aikp.modules.passwordreset.infrastructure.persistence.entity.PasswordResetEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataPasswordResetRepository
        extends JpaRepository<PasswordResetEntity, UUID> {

    Optional<PasswordResetEntity> findByToken(String token);
}\n