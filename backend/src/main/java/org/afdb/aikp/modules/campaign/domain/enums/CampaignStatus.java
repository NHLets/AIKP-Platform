package org.afdb.aikp.modules.campaign.domain.enums;

/**
 * Represents the lifecycle status of a Campaign.
 */
public enum CampaignStatus {

    /**
     * Campaign is being configured.
     */
    DRAFT,

    /**
     * Campaign has been planned and is ready to start.
     */
    PLANNED,

    /**
     * Campaign is currently active for data collection.
     */
    ACTIVE,

    /**
     * Campaign data collection period has been completed.
     */
    COMPLETED,

    /**
     * Campaign is no longer operational.
     */
    ARCHIVED
}
