package org.afdb.aikp.modules.invitation.domain.valueobject;

import java.util.UUID;

public record InvitationToken(String value) {

    public static InvitationToken generate() {
        return new InvitationToken(UUID.randomUUID().toString());
    }
}\n