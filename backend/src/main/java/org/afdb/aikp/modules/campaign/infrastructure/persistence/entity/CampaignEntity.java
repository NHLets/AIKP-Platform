package org.afdb.aikp.modules.campaign.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import org.afdb.aikp.modules.campaign.domain.enums.CampaignStatus;
import org.afdb.aikp.shared.persistence.AuditableEntity;

import java.time.LocalDate;

/**
 * JPA persistence model for the Campaign aggregate.
 *
 * <p>This class deliberately remains separate from the domain aggregate.
 * The domain model must not depend on JPA.</p>
 *
 * <p>Common persistence concerns such as the UUID identifier,
 * audit fields and optimistic locking are provided by
 * {@link AuditableEntity}.</p>
 */
@Entity
@Table(
        name = "campaign",
        schema = "campaign"
)
public class CampaignEntity extends AuditableEntity {

    @Column(
            name = "code",
            nullable = false,
            length = 50
    )
    private String code;

    @Column(
            name = "name",
            nullable = false,
            length = 255
    )
    private String name;

    @Column(
            name = "description",
            nullable = false,
            length = 1000
    )
    private String description;

    @Column(
            name = "start_date",
            nullable = false
    )
    private LocalDate startDate;

    @Column(
            name = "end_date",
            nullable = false
    )
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 30
    )
    private CampaignStatus status;

    /**
     * Required by JPA.
     */
    /**
 * Required by JPA.
 */
protected CampaignEntity() {
    super();
}

/**
 * Creates a Campaign persistence entity with an existing identifier.
 *
 * <p>Used by the persistence mapper when converting an existing
 * domain aggregate to its persistence representation.</p>
 */
public CampaignEntity(java.util.UUID id) {
    super(id);
}

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public CampaignStatus getStatus() {
        return status;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setStatus(CampaignStatus status) {
        this.status = status;
    }
}
