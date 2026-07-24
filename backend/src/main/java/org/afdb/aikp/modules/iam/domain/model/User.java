package org.afdb.aikp.modules.iam.domain.model;

import org.afdb.aikp.modules.iam.domain.enums.UserStatus;
import org.afdb.aikp.modules.iam.domain.valueobject.Email;
import org.afdb.aikp.modules.iam.domain.valueobject.FullName;
import org.afdb.aikp.modules.iam.domain.valueobject.PasswordHash;
import org.afdb.aikp.modules.iam.domain.valueobject.UserId;
import org.afdb.aikp.modules.iam.domain.valueobject.Username;

import java.time.Instant;
import java.util.Objects;

public class User {

    private final UserId id;

    private Username username;

    private Email email;

    private FullName fullName;

    private PasswordHash passwordHash;

    private UserStatus status;

    private Instant lastLogin;

    private final Instant createdAt;

    private Instant updatedAt;

    private User(
            UserId id,
            Username username,
            Email email,
            FullName fullName,
            PasswordHash passwordHash,
            UserStatus status,
            Instant createdAt,
            Instant updatedAt,
            Instant lastLogin) {

        this.id = Objects.requireNonNull(id);
        this.username = Objects.requireNonNull(username);
        this.email = Objects.requireNonNull(email);
        this.fullName = Objects.requireNonNull(fullName);
        this.passwordHash = Objects.requireNonNull(passwordHash);
        this.status = Objects.requireNonNull(status);
        this.createdAt = Objects.requireNonNull(createdAt);
        this.updatedAt = Objects.requireNonNull(updatedAt);
        this.lastLogin = lastLogin;
    }

    public static User create(
            Username username,
            Email email,
            FullName fullName,
            PasswordHash passwordHash) {

        Instant now = Instant.now();

        return new User(
                UserId.generate(),
                username,
                email,
                fullName,
                passwordHash,
                UserStatus.PENDING,
                now,
                now,
                null);
    }

    public static User restore(
            UserId id,
            Username username,
            Email email,
            FullName fullName,
            PasswordHash passwordHash,
            UserStatus status,
            Instant createdAt,
            Instant updatedAt,
            Instant lastLogin) {

        return new User(
                id,
                username,
                email,
                fullName,
                passwordHash,
                status,
                createdAt,
                updatedAt,
                lastLogin);
    }

    public UserId getId() {
        return id;
    }

    public Username getUsername() {
        return username;
    }

    public Email getEmail() {
        return email;
    }

    public FullName getFullName() {
        return fullName;
    }

    public PasswordHash getPasswordHash() {
        return passwordHash;
    }

    public UserStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getLastLogin() {
        return lastLogin;
    }
}