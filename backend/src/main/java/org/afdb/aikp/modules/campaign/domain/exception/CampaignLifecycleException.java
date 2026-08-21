package org.afdb.aikp.modules.campaign.domain.exception;

import org.afdb.aikp.shared.exception.ConflictException;

/**
 * Thrown when an invalid Campaign lifecycle transition is requested.
 */
public class CampaignLifecycleException extends ConflictException {

    public CampaignLifecycleException(String message) {
        super(message);
    }
}
