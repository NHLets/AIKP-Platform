package org.afdb.aikp.modules.campaign.domain.exception;

import org.afdb.aikp.shared.exception.ConflictException;

public class CampaignCodeAlreadyExistsException
        extends ConflictException {

    public CampaignCodeAlreadyExistsException(String message) {
        super(message);
    }
}