package org.afdb.aikp.modules.passwordreset.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.afdb.aikp.modules.passwordreset.domain.enums.PasswordResetStatus;
import org.afdb.aikp.modules.passwordreset.domain.model.PasswordReset;
import org.afdb.aikp.modules.passwordreset.domain.valueobject.PasswordResetId;
import org.afdb.aikp.modules.passwordreset.domain.valueobject.ResetToken;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "password_reset", schema = "reference")
public class PasswordResetEntity {

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
    private Instant completedAt;

    public PasswordResetEntity() {}

    public static PasswordResetEntity fromDomain(PasswordReset reset) {
        PasswordResetEntity e = new PasswordResetEntity();
        e.id = reset.getId().value();
        e.userId = reset.getUserId();
        e.token = reset.getToken().value();
        e.status = reset.getStatus().name();
        e.expiresAt = reset.getExpiresAt();

        try {
            Field createdField = PasswordReset.class.getDeclaredField("createdAt");
            createdField.setAccessible(true);
            e.createdAt = (Instant) createdField.get(reset);

            Field completedField = PasswordReset.class.getDeclaredField("completedAt");
            completedField.setAccessible(true);
            e.completedAt = (Instant) completedField.get(reset);

        } catch (ReflectiveOperationException ex) {
            throw new IllegalStateException("Unable to map PasswordReset domain", ex);
        }

        return e;
    }

    public PasswordReset toDomain() {
        PasswordReset reset = new PasswordReset(
                new PasswordResetId(id),
                userId,
                new ResetToken(token),
                expiresAt,
                createdAt
        );

        try {
            Field statusField = PasswordReset.class.getDeclaredField("status");
            statusField.setAccessible(true);
            statusField.set(reset, PasswordResetStatus.valueOf(status));

            Field completedField = PasswordReset.class.getDeclaredField("completedAt");
            completedField.setAccessible(true);
            completedField.set(reset, completedAt);

        } catch (ReflectiveOperationException ex) {
            throw new IllegalStateException("Unable to map PasswordResetEntity", ex);
        }

        return reset;
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

    public Instant getCompletedAt() { return completedAt; }
    public void setCompletedAt(Instant completedAt) { this.completedAt = completedAt; }
}
