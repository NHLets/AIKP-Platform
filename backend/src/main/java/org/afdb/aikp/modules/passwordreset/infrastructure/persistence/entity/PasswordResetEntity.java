package org.afdb.aikp.modules.passwordreset.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="password_reset")
public class PasswordResetEntity {

    @Id
    private UUID id;

    @Column(nullable=false)
    private UUID userId;

    @Column(nullable=false, unique=true, length=120)
    private String token;

    @Column(nullable=false, length=20)
    private String status;

    private Instant expiresAt;
    private Instant createdAt;
    private Instant completedAt;
}\n