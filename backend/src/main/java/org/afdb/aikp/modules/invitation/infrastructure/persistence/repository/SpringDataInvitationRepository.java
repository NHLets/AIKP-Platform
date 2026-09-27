package org.afdb.aikp.modules.invitation.infrastructure.persistence.repository;

import org.afdb.aikp.modules.invitation.infrastructure.persistence.entity.InvitationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataInvitationRepository
        extends JpaRepository<InvitationEntity, UUID> {

    Optional<InvitationEntity> findByToken(String token);
}\n