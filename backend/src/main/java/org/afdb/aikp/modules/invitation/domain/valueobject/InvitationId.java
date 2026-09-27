package org.afdb.aikp.modules.invitation.domain.valueobject;

import java.util.UUID;

public record InvitationId(UUID value) {

    public static InvitationId generate() {
        return new InvitationId(UUID.randomUUID());
    }
}\n