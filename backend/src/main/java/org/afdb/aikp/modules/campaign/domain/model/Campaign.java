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
 *
 * <p>The Campaign lifecycle is strictly controlled by the following
 * transitions:</p>
 *
 * <pre>
 * DRAFT -> PLANNED -> ACTIVE -> COMPLETED -> ARCHIVED
 * </pre>
 *
 * <p>The Campaign status is the single source of truth for its lifecycle
 * state. There is deliberately no separate {@code active} flag.</p>
 */
public class Campaign extends AggregateRoot<CampaignId> {

    private final CampaignCode code;

    private CampaignName name;

    private CampaignDescription description;

    private LocalDate startDate;

    private LocalDate endDate;

    private CampaignStatus status;

    private Campaign(
            CampaignId id,
            CampaignCode code,
            CampaignName name,
            CampaignDescription description,
            LocalDate startDate,
            LocalDate endDate,
            CampaignStatus status) {

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
    }

    /**
     * Creates a new Campaign.
     *
     * <p>A newly created Campaign always starts in {@link CampaignStatus#DRAFT}.
     * </p>
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
                CampaignStatus.DRAFT);
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
            CampaignStatus status) {

        validateDates(startDate, endDate);

        return new Campaign(
                id,
                code,
                name,
                description,
                startDate,
                endDate,
                status);
    }

    /**
     * Changes the Campaign name.
     */
    public void rename(CampaignName newName) {

        this.name = Objects.requireNonNull(
                newName,
                "Campaign name cannot be null.");
    }

    /**
     * Changes the Campaign description.
     */
    public void changeDescription(
            CampaignDescription newDescription) {

        this.description = Objects.requireNonNull(
                newDescription,
                "Campaign description cannot be null.");
    }

    /**
     * Changes the Campaign period.
     *
     * <p>The period can only be changed while the Campaign is in
     * {@link CampaignStatus#DRAFT} or {@link CampaignStatus#PLANNED}.</p>
     *
     * @throws CampaignLifecycleException if the Campaign is already
     *         ACTIVE, COMPLETED or ARCHIVED
     */
    public void changePeriod(
            LocalDate newStartDate,
            LocalDate newEndDate) {

        ensurePeriodCanBeChanged();

        validateDates(newStartDate, newEndDate);

        this.startDate = newStartDate;
        this.endDate = newEndDate;
    }

    /**
     * Moves the Campaign from DRAFT to PLANNED.
     */
    public void plan() {

        if (status != CampaignStatus.DRAFT) {
            throw new CampaignLifecycleException(
                    "Only draft campaigns can be planned.");
        }

        status = CampaignStatus.PLANNED;
    }

    /**
     * Moves the Campaign from PLANNED to ACTIVE.
     */
    public void activate() {

        if (status != CampaignStatus.PLANNED) {
            throw new CampaignLifecycleException(
                    "Only planned campaigns can be activated.");
        }

        status = CampaignStatus.ACTIVE;
    }

    /**
     * Moves the Campaign from ACTIVE to COMPLETED.
     */
    public void complete() {

        if (status != CampaignStatus.ACTIVE) {
            throw new CampaignLifecycleException(
                    "Only active campaigns can be completed.");
        }

        status = CampaignStatus.COMPLETED;
    }

    /**
     * Moves the Campaign from COMPLETED to ARCHIVED.
     */
    public void archive() {

        if (status != CampaignStatus.COMPLETED) {
            throw new CampaignLifecycleException(
                    "Only completed campaigns can be archived.");
        }

        status = CampaignStatus.ARCHIVED;
    }

    /**
     * Returns whether the Campaign is currently active.
     *
     * <p>The value is derived exclusively from the Campaign status.
     * There is no independent active flag.</p>
     */
    public boolean isActive() {
        return status == CampaignStatus.ACTIVE;
    }

    /**
     * Ensures that the Campaign period can still be modified.
     */
    private void ensurePeriodCanBeChanged() {

        if (status != CampaignStatus.DRAFT
                && status != CampaignStatus.PLANNED) {

            throw new CampaignLifecycleException(
                    "Campaign period can only be changed "
                            + "while the campaign is draft or planned.");
        }
    }

    /**
     * Validates the Campaign period.
     */
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
}