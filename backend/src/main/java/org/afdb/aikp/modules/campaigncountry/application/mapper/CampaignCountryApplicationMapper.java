package org.afdb.aikp.modules.campaigncountry.application.mapper;

import org.afdb.aikp.modules.campaigncountry.application.response.CampaignCountryResponse;
import org.afdb.aikp.modules.campaigncountry.domain.model.CampaignCountry;

import org.springframework.stereotype.Component;

/**
 * Maps CampaignCountry domain objects
 * to application responses.
 */
@Component
public class CampaignCountryApplicationMapper {

    public CampaignCountryResponse toResponse(
            CampaignCountry campaignCountry) {

        return new CampaignCountryResponse(
                campaignCountry.getId().getValue(),
                campaignCountry.getCampaignId().getValue(),
                campaignCountry.getCountryId().getValue(),
                campaignCountry.getCreatedAt(),
                campaignCountry.getUpdatedAt()
        );
    }
}
