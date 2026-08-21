package org.afdb.aikp.modules.campaign.domain.exception;

import org.afdb.aikp.shared.exception.NotFoundException;

public class CampaignNotFoundException extends NotFoundException {

    public CampaignNotFoundException(String message) {
        super(message);
    }
}