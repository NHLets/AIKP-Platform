package org.afdb.aikp.modules.invitation.application.service;

import org.afdb.aikp.modules.invitation.domain.model.Invitation;
import org.afdb.aikp.modules.invitation.domain.repository.InvitationRepository;
import org.afdb.aikp.modules.invitation.domain.valueobject.InvitationId;
import org.afdb.aikp.modules.invitation.domain.valueobject.InvitationToken;
import org.afdb.aikp.modules.iam.domain.repository.UserRepository;
import org.afdb.aikp.modules.iam.domain.valueobject.UserId;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class InvitationApplicationService {

    private final InvitationRepository invitationRepository;
    private final UserRepository userRepository;

    public InvitationApplicationService(
            InvitationRepository invitationRepository,
            UserRepository userRepository) {
        this.invitationRepository = invitationRepository;
        this.userRepository = userRepository;
    }


    public InvitationToken createInvitation(UUID userId) {

        userRepository.findById(UserId.of(userId))
                .orElseThrow();

        Invitation invitation = new Invitation(
                InvitationId.generate(),
                userId,
                InvitationToken.generate(),
                Instant.now().plus(72, ChronoUnit.HOURS),
                Instant.now()
        );

        invitationRepository.save(invitation);

        return invitation.getToken();
    }
}
