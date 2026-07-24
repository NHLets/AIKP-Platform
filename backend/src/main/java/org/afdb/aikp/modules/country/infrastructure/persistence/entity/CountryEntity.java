package org.afdb.aikp.modules.country.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import org.afdb.aikp.shared.persistence.AuditableEntity;

import java.util.UUID;

/**
 * JPA persistence entity for Country.
 */
@Entity
@Table(name = "country", schema = "reference")
public class CountryEntity extends AuditableEntity {

    @Column(name = "iso2_code", nullable = false, unique = true, length = 2)
    private String iso2Code;

    @Column(name = "iso3_code", nullable = false, unique = true, length = 3)
    private String iso3Code;

    @Column(name = "numeric_code", nullable = false, unique = true, length = 3)
    private String numericCode;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "official_name", nullable = false, length = 200)
    private String officialName;

    @Column(name = "active", nullable = false)
    private boolean active;

    /**
     * Required by JPA.
     */
    protected CountryEntity() {
    }

    /**
     * Full constructor used by the persistence mapper.
     */
    public CountryEntity(
            UUID id,
            String iso2Code,
            String iso3Code,
            String numericCode,
            String name,
            String officialName,
            boolean active) {

        super(id);

        this.iso2Code = iso2Code;
        this.iso3Code = iso3Code;
        this.numericCode = numericCode;
        this.name = name;
        this.officialName = officialName;
        this.active = active;
    }

    public String getIso2Code() {
        return iso2Code;
    }

    public void setIso2Code(String iso2Code) {
        this.iso2Code = iso2Code;
    }

    public String getIso3Code() {
        return iso3Code;
    }

    public void setIso3Code(String iso3Code) {
        this.iso3Code = iso3Code;
    }

    public String getNumericCode() {
        return numericCode;
    }

    public void setNumericCode(String numericCode) {
        this.numericCode = numericCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOfficialName() {
        return officialName;
    }

    public void setOfficialName(String officialName) {
        this.officialName = officialName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}