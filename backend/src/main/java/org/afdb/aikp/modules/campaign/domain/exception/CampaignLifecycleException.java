package org.afdb.aikp.modules.campaign.domain.exception;

/**
 * Thrown when an invalid Campaign lifecycle transition is requested.
 */
public class CampaignLifecycleException extends RuntimeException {

    public CampaignLifecycleException(String message) {
        super(message);
    }
}
