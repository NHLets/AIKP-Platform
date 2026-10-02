package org.afdb.aikp.modules.passwordreset.domain.model;

import org.afdb.aikp.modules.passwordreset.domain.enums.PasswordResetStatus;
import org.afdb.aikp.modules.passwordreset.domain.valueobject.PasswordResetId;
import org.afdb.aikp.modules.passwordreset.domain.valueobject.ResetToken;

import java.time.Instant;
import java.util.UUID;

public class PasswordReset {

    private final PasswordResetId id;
    private final UUID userId;
    private final ResetToken token;
    private PasswordResetStatus status;
    private final Instant expiresAt;
    private final Instant createdAt;
    private Instant completedAt;

    public PasswordReset(
            PasswordResetId id,
            UUID userId,
            ResetToken token,
            Instant expiresAt,
            Instant createdAt) {

        this.id = id;
        this.userId = userId;
        this.token = token;
        this.status = PasswordResetStatus.PENDING;
        this.expiresAt = expiresAt;
        this.createdAt = createdAt;
    }

    public PasswordResetId getId(){ return id; }
    public UUID getUserId(){ return userId; }
    public ResetToken getToken(){ return token; }
    public PasswordResetStatus getStatus(){ return status; }
    public Instant getExpiresAt(){ return expiresAt; }

    public void complete(){
        status = PasswordResetStatus.COMPLETED;
        completedAt = Instant.now();
    }

    public void expire(){
        status = PasswordResetStatus.EXPIRED;
    }
}
