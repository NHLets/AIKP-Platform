package org.afdb.aikp.modules.campaigncountry.domain.exception;

import java.util.UUID;

/**
 * Thrown when a CampaignCountry association
 * cannot be found.
 */
public class CampaignCountryNotFoundException
        extends RuntimeException {

    public CampaignCountryNotFoundException(
            UUID campaignId,
            UUID countryId) {

        super(
                "Country with ID " + countryId
                        + " is not associated with campaign "
                        + campaignId + "."
        );
    }
}