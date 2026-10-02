package org.afdb.aikp.modules.invitation.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.afdb.aikp.modules.invitation.domain.enums.InvitationStatus;
import org.afdb.aikp.modules.invitation.domain.model.Invitation;
import org.afdb.aikp.modules.invitation.domain.valueobject.InvitationId;
import org.afdb.aikp.modules.invitation.domain.valueobject.InvitationToken;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "invitation", schema = "reference")
public class InvitationEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false, unique = true, length = 120)
    private String token;

    @Column(nullable = false, length = 20)
    private String status;

    private Instant expiresAt;
    private Instant createdAt;
    private Instant acceptedAt;

    public InvitationEntity() {}

    public static InvitationEntity fromDomain(Invitation invitation) {
        InvitationEntity e = new InvitationEntity();
        e.id = invitation.getId().value();
        e.userId = invitation.getUserId();
        e.token = invitation.getToken().value();
        e.status = invitation.getStatus().name();
        e.expiresAt = invitation.getExpiresAt();
        e.createdAt = invitation.getCreatedAt();
        e.acceptedAt = invitation.getAcceptedAt();
        return e;
    }

    public Invitation toDomain() {
        Invitation invitation = new Invitation(
                new InvitationId(id),
                userId,
                new InvitationToken(token),
                expiresAt,
                createdAt
        );

        try {
            Field statusField = Invitation.class.getDeclaredField("status");
            statusField.setAccessible(true);
            statusField.set(invitation, InvitationStatus.valueOf(status));

            Field acceptedField = Invitation.class.getDeclaredField("acceptedAt");
            acceptedField.setAccessible(true);
            acceptedField.set(invitation, acceptedAt);

        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Unable to map InvitationEntity to domain", e);
        }

        return invitation;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Instant getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getAcceptedAt() { return acceptedAt; }
    public void setAcceptedAt(Instant acceptedAt) { this.acceptedAt = acceptedAt; }
}
