package org.afdb.aikp.modules.campaigncountry.domain.exception;

import java.util.UUID;

/**
 * Thrown when a Country is already assigned
 * to a Campaign.
 */
public class CountryAlreadyAssignedToCampaignException
        extends RuntimeException {

    public CountryAlreadyAssignedToCampaignException(
            UUID campaignId,
            UUID countryId) {

        super(
                "Country "
                        + countryId
                        + " is already assigned to campaign "
                        + campaignId);
    }
}
