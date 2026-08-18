package org.afdb.aikp.modules.campaign.domain.model;

import org.afdb.aikp.modules.campaign.domain.enums.CampaignStatus;
import org.afdb.aikp.modules.campaign.domain.exception.CampaignLifecycleException;
import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignCode;
import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignDescription;
import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignId;
import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignName;
import org.afdb.aikp.shared.domain.AggregateRoot;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Aggregate Root representing an AIKP data collection Campaign.
 */
public class Campaign extends AggregateRoot<CampaignId> {

    private final CampaignCode code;

    private CampaignName name;

    private CampaignDescription description;

    private LocalDate startDate;

    private LocalDate endDate;

    private CampaignStatus status;

    private boolean active;

    private Campaign(
            CampaignId id,
            CampaignCode code,
            CampaignName name,
            CampaignDescription description,
            LocalDate startDate,
            LocalDate endDate,
            CampaignStatus status,
            boolean active) {

        super(Objects.requireNonNull(
                id,
                "Campaign id cannot be null."));

        this.code = Objects.requireNonNull(
                code,
                "Campaign code cannot be null.");

        this.name = Objects.requireNonNull(
                name,
                "Campaign name cannot be null.");

        this.description = Objects.requireNonNull(
                description,
                "Campaign description cannot be null.");

        this.startDate = Objects.requireNonNull(
                startDate,
                "Campaign start date cannot be null.");

        this.endDate = Objects.requireNonNull(
                endDate,
                "Campaign end date cannot be null.");

        this.status = Objects.requireNonNull(
                status,
                "Campaign status cannot be null.");

        this.active = active;
    }

    /**
     * Creates a new Campaign.
     */
    public static Campaign create(
            CampaignCode code,
            CampaignName name,
            CampaignDescription description,
            LocalDate startDate,
            LocalDate endDate) {

        validateDates(startDate, endDate);

        return new Campaign(
                CampaignId.generate(),
                code,
                name,
                description,
                startDate,
                endDate,
                CampaignStatus.DRAFT,
                true);
    }

    /**
     * Restores an existing Campaign from persistence.
     */
    public static Campaign restore(
            CampaignId id,
            CampaignCode code,
            CampaignName name,
            CampaignDescription description,
            LocalDate startDate,
            LocalDate endDate,
            CampaignStatus status,
            boolean active) {

        validateDates(startDate, endDate);

        return new Campaign(
                id,
                code,
                name,
                description,
                startDate,
                endDate,
                status,
                active);
    }

    public void rename(CampaignName newName) {
        this.name = Objects.requireNonNull(
                newName,
                "Campaign name cannot be null.");
    }

    public void changeDescription(
            CampaignDescription newDescription) {

        this.description = Objects.requireNonNull(
                newDescription,
                "Campaign description cannot be null.");
    }

    public void changePeriod(
            LocalDate newStartDate,
            LocalDate newEndDate) {

        validateDates(newStartDate, newEndDate);

        this.startDate = newStartDate;
        this.endDate = newEndDate;
    }

    public void plan() {

        if (status != CampaignStatus.DRAFT) {
            throw new CampaignLifecycleException(
                    "Only draft campaigns can be planned.");
        }

        status = CampaignStatus.PLANNED;
    }

    public void activate() {

        if (status != CampaignStatus.PLANNED) {
            throw new CampaignLifecycleException(
                    "Only planned campaigns can be activated.");
        }

        status = CampaignStatus.ACTIVE;
        active = true;
    }

    public void complete() {

        if (status != CampaignStatus.ACTIVE) {
            throw new CampaignLifecycleException(
                    "Only active campaigns can be completed.");
        }

        status = CampaignStatus.COMPLETED;
        active = false;
    }

    public void archive() {

        if (status != CampaignStatus.COMPLETED) {
            throw new CampaignLifecycleException(
                    "Only completed campaigns can be archived.");
        }

        status = CampaignStatus.ARCHIVED;
        active = false;
    }

    public void deactivate() {
        active = false;
    }

    private static void validateDates(
            LocalDate startDate,
            LocalDate endDate) {

        Objects.requireNonNull(
                startDate,
                "Campaign start date cannot be null.");

        Objects.requireNonNull(
                endDate,
                "Campaign end date cannot be null.");

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException(
                    "Campaign end date cannot be before start date.");
        }
    }

    public CampaignCode getCode() {
        return code;
    }

    public CampaignName getName() {
        return name;
    }

    public CampaignDescription getDescription() {
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

    public boolean isActive() {
        return active;
    }
}
