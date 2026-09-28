package org.afdb.aikp.modules.iam.domain.model;

import org.afdb.aikp.modules.iam.domain.enums.Role;
import org.afdb.aikp.modules.iam.domain.valueobject.UserId;

import java.time.Instant;
import java.util.Objects;

public class User {

    private final UserId id;
    private final String username;
    private final String email;
    private String passwordHash;
    private Role role;
    private boolean active;
    private final Instant createdAt;

    public User(
            UserId id,
            String username,
            String email,
            String passwordHash,
            Role role,
            Instant createdAt) {

        this.id = Objects.requireNonNull(id);
        this.username = Objects.requireNonNull(username);
        this.email = Objects.requireNonNull(email);
        this.passwordHash = Objects.requireNonNull(passwordHash);
        this.role = Objects.requireNonNull(role);
        this.createdAt = Objects.requireNonNull(createdAt);
        this.active = true;
    }

    public UserId getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public Role getRole() { return role; }
    public boolean isActive() { return active; }
    public Instant getCreatedAt() { return createdAt; }

    public void activate() {
        active = true;
    }

    public void deactivate() {
        active = false;
    }

    public void changeRole(Role newRole) {
        role = Objects.requireNonNull(newRole);
    }

    public void updatePasswordHash(String newHash) {
        passwordHash = Objects.requireNonNull(newHash);
    }
}
