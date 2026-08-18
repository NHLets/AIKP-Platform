package org.afdb.aikp.modules.organization.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationType;
import org.afdb.aikp.shared.persistence.AuditableEntity;

import java.util.UUID;

/**
 * JPA persistence entity for Organization.
 */
@Entity
@Table(name = "organization", schema = "reference")
public class OrganizationEntity extends AuditableEntity {

    @Column(
            name = "code",
            nullable = false,
            unique = true,
            length = 50
    )
    private String code;

    @Column(
            name = "name",
            nullable = false,
            length = 255
    )
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "type",
            nullable = false,
            length = 30
    )
    private OrganizationType type;

    @Column(
            name = "country_id",
            nullable = false
    )
    private UUID countryId;

    @Column(
            name = "active",
            nullable = false
    )
    private boolean active;

    /**
     * Required by JPA.
     */
    protected OrganizationEntity() {
    }

    /**
     * Full constructor used by the persistence mapper.
     */
    public OrganizationEntity(
            UUID id,
            String code,
            String name,
            OrganizationType type,
            UUID countryId,
            boolean active) {

        super(id);

        this.code = code;
        this.name = name;
        this.type = type;
        this.countryId = countryId;
        this.active = active;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OrganizationType getType() {
        return type;
    }

    public void setType(OrganizationType type) {
        this.type = type;
    }

    public UUID getCountryId() {
        return countryId;
    }

    public void setCountryId(UUID countryId) {
        this.countryId = countryId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
