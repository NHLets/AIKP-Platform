package org.afdb.aikp.modules.person.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

/**
 * JPA entity representing a person officially associated
 * with an organization.
 */
@Entity
@Table(
        name = "person",
        schema = "reference")
public class PersonEntity {

    @Id
    @Column(
            name = "id",
            nullable = false,
            updatable = false)
    private UUID id;

    @Column(
            name = "full_name",
            nullable = false,
            length = 255)
    private String fullName;

    @Column(
            name = "organization_id",
            nullable = false)
    private UUID organizationId;

    @Column(
            name = "active",
            nullable = false)
    private boolean active;

    protected PersonEntity() {
        // Required by JPA.
    }

    public PersonEntity(
            UUID id,
            String fullName,
            UUID organizationId,
            boolean active) {

        this.id = id;
        this.fullName = fullName;
        this.organizationId = organizationId;
        this.active = active;
    }

    public UUID getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public UUID getOrganizationId() {
        return organizationId;
    }

    public boolean isActive() {
        return active;
    }
}
