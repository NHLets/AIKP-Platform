package org.afdb.aikp.modules.iam.role.domain.model;

import org.afdb.aikp.modules.iam.role.domain.enums.RoleStatus;
import org.afdb.aikp.modules.iam.role.domain.valueobject.RoleDescription;
import org.afdb.aikp.modules.iam.role.domain.valueobject.RoleId;
import org.afdb.aikp.modules.iam.role.domain.valueobject.RoleName;

import java.time.Instant;
import java.util.Objects;

public class Role {

    private final RoleId id;

    private RoleName name;

    private RoleDescription description;

    private RoleStatus status;

    private final boolean system;

    private final Instant createdAt;

    private Instant updatedAt;

    private Role(
            RoleId id,
            RoleName name,
            RoleDescription description,
            RoleStatus status,
            boolean system,
            Instant createdAt,
            Instant updatedAt) {

        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.description = Objects.requireNonNull(description);
        this.status = Objects.requireNonNull(status);
        this.system = system;
        this.createdAt = Objects.requireNonNull(createdAt);
        this.updatedAt = Objects.requireNonNull(updatedAt);
    }

    public static Role create(
            RoleName name,
            RoleDescription description,
            boolean system) {

        Instant now = Instant.now();

        return new Role(
                RoleId.generate(),
                name,
                description,
                RoleStatus.ACTIVE,
                system,
                now,
                now);
    }

    public static Role restore(
            RoleId id,
            RoleName name,
            RoleDescription description,
            RoleStatus status,
            boolean system,
            Instant createdAt,
            Instant updatedAt) {

        return new Role(
                id,
                name,
                description,
                status,
                system,
                createdAt,
                updatedAt);
    }

    public RoleId getId() {
        return id;
    }

    public RoleName getName() {
        return name;
    }

    public RoleDescription getDescription() {
        return description;
    }

    public RoleStatus getStatus() {
        return status;
    }

    public boolean isSystem() {
        return system;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void activate() {

        if (status == RoleStatus.ACTIVE) {
            return;
        }

        status = RoleStatus.ACTIVE;
        touch();
    }

    public void deactivate() {

        if (status == RoleStatus.INACTIVE) {
            return;
        }

        status = RoleStatus.INACTIVE;
        touch();
    }

    public void rename(RoleName name) {
        this.name = Objects.requireNonNull(name);
        touch();
    }

    public void changeDescription(RoleDescription description) {
        this.description = Objects.requireNonNull(description);
        touch();
    }

    private void touch() {
        updatedAt = Instant.now();
    }
}