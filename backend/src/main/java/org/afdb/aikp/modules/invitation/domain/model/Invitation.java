package org.afdb.aikp.modules.invitation.domain.model;

import org.afdb.aikp.modules.invitation.domain.enums.InvitationStatus;
import org.afdb.aikp.modules.invitation.domain.valueobject.InvitationId;
import org.afdb.aikp.modules.invitation.domain.valueobject.InvitationToken;

import java.time.Instant;
import java.util.UUID;

public class Invitation {

    private final InvitationId id;
    private final UUID userId;
    private final InvitationToken token;
    private InvitationStatus status;
    private final Instant expiresAt;
    private final Instant createdAt;
    private Instant acceptedAt;

    public Invitation(
            InvitationId id,
            UUID userId,
            InvitationToken token,
            Instant expiresAt,
            Instant createdAt) {
        this.id = id;
        this.userId = userId;
        this.token = token;
        this.status = InvitationStatus.PENDING;
        this.expiresAt = expiresAt;
        this.createdAt = createdAt;
    }

    public InvitationId getId() { return id; }
    public UUID getUserId() { return userId; }
    public InvitationToken getToken() { return token; }
    public InvitationStatus getStatus() { return status; }
    public Instant getExpiresAt() { return expiresAt; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getAcceptedAt() { return acceptedAt; }

    public void accept() {
        status = InvitationStatus.ACCEPTED;
        acceptedAt = Instant.now();
    }

    public void expire() {
        status = InvitationStatus.EXPIRED;
    }
}
