package org.afdb.aikp.modules.invitation.domain.repository;

import org.afdb.aikp.modules.invitation.domain.model.Invitation;
import org.afdb.aikp.modules.invitation.domain.valueobject.InvitationToken;

import java.util.Optional;
import java.util.UUID;

public interface InvitationRepository {

    Invitation save(Invitation invitation);

    Optional<Invitation> findById(UUID id);

    Optional<Invitation> findByToken(InvitationToken token);

    void delete(UUID id);
}\n